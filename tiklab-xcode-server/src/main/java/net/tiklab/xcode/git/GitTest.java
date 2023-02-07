package net.tiklab.xcode.git;

import net.tiklab.xcode.authority.SshCommandFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;


import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.util.Enumeration;

public class GitTest {


    public static void main(String[] args) throws Exception {
        // sshServer();

        findIp();


    }

    private static void sshServer() throws IOException, InterruptedException {
        SshServer sshServer = SshServer.setUpDefaultServer();
        sshServer.setPort(8083);
        sshServer.setHost ("0.0.0.0");
        sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get("C:\\Users\\admin\\.ssh\\id_rsa")));
        sshServer.setPasswordAuthenticator((username, password, session) -> true);
        sshServer.start();
        sshServer.setCommandFactory(new SshCommandFactory());

        Thread.sleep(2000000);
    }

    private static void findIp() throws IOException {


        // try {
        //     Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        //     while (interfaces.hasMoreElements()) {
        //         NetworkInterface ni = interfaces.nextElement();
        //         Enumeration<InetAddress> addresses = ni.getInetAddresses();
        //         while (addresses.hasMoreElements()) {
        //             InetAddress address = addresses.nextElement();
        //             if (!address.isLinkLocalAddress() && !address.isLoopbackAddress() && !address.getHostAddress().contains(":")) {
        //                 System.out.println("本机 IP 地址：" + address.getHostAddress());
        //                 break;
        //             }
        //         }
        //     }
        // } catch (SocketException e) {
        //     e.printStackTrace();
        // }


    }


























}








































