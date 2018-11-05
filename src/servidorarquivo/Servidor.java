/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorarquivo;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LuizV1
 */
public class Servidor {

    public static boolean ligar = false;
    
    public static String logArquivo ="";

    public static void main(String[] args) throws IOException {
        FrmInterface tela = new FrmInterface();
        tela.setVisible(true);

    }

    public static Runnable receberArquivoNomeado = new Runnable() {
        @Override
        public void run() {
            
                System.out.println("receberArquivoNomeado em execução");
                try {
                    int bytesRead;
                    int current = 0;

                    ServerSocket serverSocket = null;
                    serverSocket = new ServerSocket(5005);
                    
                    String pastaPadrao = "C:/Synchro Relatorios";

                    Path diretorioPasta = Paths.get(pastaPadrao);
                    
                    if (!Files.exists(diretorioPasta)) {
                        Files.createDirectory(diretorioPasta);
                    }

                    while (true) {
                        Socket clientSocket = null;
                        clientSocket = serverSocket.accept();

                        InputStream in = clientSocket.getInputStream();

                        DataInputStream clientData = new DataInputStream(in);

                        String fileName = clientData.readUTF();
                        OutputStream output = new FileOutputStream("C:\\Synchro Relatorios\\" + fileName);
                        long size = clientData.readLong();
                        byte[] buffer = new byte[1024];
                        while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                            output.write(buffer, 0, bytesRead);
                            size -= bytesRead;
                        }
                        
                        logArquivo = output.toString();
                       

                        // Closing the FileOutputStream handle
                        in.close();
                        clientData.close();
                        output.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
    };

    public static Runnable t1 = new Runnable() {
        @Override
        public void run() {
            while (ligar) {
                try {
                    ServerSocket serverSocket = null;

                    try {
                        serverSocket = new ServerSocket(5005);
                    } catch (IOException ex) {
                        System.out.println("Can't setup server on this port number. ");
                    }

                    Socket socket = null;
                    InputStream in = null;
                    OutputStream out = null;

                    try {
                        socket = serverSocket.accept();
                    } catch (IOException ex) {
                        System.out.println("Can't accept client connection. ");
                    }

                    try {
                        in = socket.getInputStream();
                    } catch (IOException ex) {
                        System.out.println("Can't get socket input stream. ");
                    }

                    try {
                        out = new FileOutputStream("C:\\Synchro Relatorios\\teste.txt");
                    } catch (FileNotFoundException ex) {
                        System.out.println("File not found. ");
                    }

                    byte[] bytes = new byte[16 * 1024];

                    int count;
                    while ((count = in.read(bytes)) > 0) {
                        out.write(bytes, 0, count);
                    }

                    out.close();
                    in.close();
                    socket.close();
                    serverSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

}
