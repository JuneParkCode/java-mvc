package webserver.core;

public abstract class RequestHandler extends Thread{
    public abstract void handle();

    @Override
    public void run() {
        handle();
    }
}
