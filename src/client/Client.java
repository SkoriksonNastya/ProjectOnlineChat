package client;

import logger.Logger;
import configuration.Config;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final Logger LOG = Logger.getLog();
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner sc;

    public Client() {
        try {
            final Config cfg = Config.getInstance();
            sc = new Scanner(System.in);
            socket = new Socket(cfg.getHost(), cfg.getPort());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            System.out.println("Добро пожаловать! Введите свой никнейм");
            LOG.log("Вход нового клиента");
            out.println(sc.nextLine());

            final Receiver receiver = new Receiver(in);
            receiver.start();

            String msg = "";
            while (!"exit".equals(msg)) {
                msg = sc.nextLine();
                out.println(msg);
            }

            receiver.interrupt();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            LOG.log("Поток закрыт" + Client.class.getName());
            closeAll();
        }
    }

    private void closeAll() {
        try {
            sc.close();
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
