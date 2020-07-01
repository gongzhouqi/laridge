package services2.client;

import com.google.gson.Gson;
import models.RoomAccessModel;
import models.RoomWaitModel;
import services2.Core;
import services2.models.*;
import user.User;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RoomClientCore extends Core {

    private String roomId;

    private int ownerIP;

    private int ownerPort;

    private AtomicBoolean isWaiting;

    private RoomAccessModel rm;

    private RoomWaitModel waiter;

    private RoomClientCore() {}

    public static RoomClientCore guestRoom(RoomAccessModel rm) {
        RoomClientCore client = new RoomClientCore();
        client.roomId = rm.getRoomId();
        client.ownerIP = rm.getOwnerIP();
        client.ownerPort = rm.getOwnerPort();
        client.isWaiting = new AtomicBoolean(true);
        client.rm = rm;
        RoomConnectModel model = new RoomConnectModel(
                rm.getRoomId(),
                User.getSingleton().getUsername(),
                User.getSingleton().getHeadId()
        );
        client.send(model, rm.getOwnerIP(), rm.getOwnerPort());
        try {
            rm.getLock().lock();
            rm.getWaitCondition().await(10, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        } finally {
            rm.getLock().unlock();
            client.isWaiting.set(false);
            client.rm = null;
        }
        if (rm.getResponse() != null && rm.getResponse().equals("OK")) {
            return client;
        } else {
            return null;
        }
    }

    @Override
    public void process(Model model, int replyIP, int replyPort) {
        if (model instanceof AccessResultModel) {
            processAccessResult((AccessResultModel)model);
        } else if (model instanceof RoomArrangeModel) {
            processRoomArrange((RoomArrangeModel)model);
        } else if (model instanceof GameStartModel) {
            processGameStart((GameStartModel)model);
        }
        // TODO:
    }

    private void processAccessResult(AccessResultModel model) {
        if (isWaiting.get()) {
            if (model.isAccepted()) {
                rm.setResponse("OK");
            } else {
                rm.setResponse("NO");
            }
            rm.getLock().lock();
            rm.getWaitCondition().signal();
            rm.getLock().unlock();
        }
    }

    @Override
    public void shutDown() {
        signalFrontEnd("QUIT");
        super.shutDown();
    }

    private void processRoomArrange(RoomArrangeModel model) {
        Gson g = new Gson();
        signalFrontEnd(g.toJson(model));
    }

    private void processGameStart(GameStartModel model) {
        signalFrontEnd("START");
        User.getSingleton().loadGame(model.getGameId(), model.getServerIP(), model.getServerPort());
    }

    private void signalFrontEnd(String message) {
        if (isWaiting.get()) {
            waiter.setResponse(message);
            waiter.getLock().lock();
            waiter.getWaitCondition().signal();
            waiter.getLock().unlock();
        }
    }

    public void waitOn(RoomWaitModel waiter) {
        this.isWaiting.set(true);
        this.waiter = waiter;
        this.waiter.getLock().lock();
        try {
            while (waiter.getResponse() == null) {
                this.waiter.getWaitCondition().await();
            }
        } catch (InterruptedException ignored) {
        } finally {
            this.waiter.getLock().unlock();
            this.isWaiting.set(false);
            this.waiter = null;
        }
    }

}
