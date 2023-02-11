package net.tiklab.xcode.git;

import net.tiklab.xcode.authority.SshCommandFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;

public class GitTest {


    public static void main(String[] args) throws Exception {
        // sshServer();

        // findIp();

        findFileTree();
    }

    private static void findFileTree() throws IOException {
        Git git = Git.open(new File("C:\\Users\\admin\\xcode\\repository\\aa.git"));

        Repository repository = git.getRepository();

        String branchName = "master";
        ObjectId head = repository.resolve("refs/heads/" + branchName);


        String commitId = "15cf56258d062bce0c1aa199b3230d3a70ec38eb";
        ObjectId commitIdObject = ObjectId.fromString(commitId);


        RevWalk walk = new RevWalk(repository);
        RevCommit commit = walk.parseCommit(commitIdObject);
        RevTree tree = commit.getTree();
        try (TreeWalk treeWalk = new TreeWalk(repository)) {
            treeWalk.addTree(tree);
            treeWalk.setRecursive(false);

            while (treeWalk.next()) {
                FileMode fileMode = treeWalk.getFileMode();
                // treeWalk.getTree()
                System.out.println("found: " + treeWalk.getPathString());
            }
        }


        // Git.wrap()



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

        try (Git git =  Git.open(new File("C:\\Users\\admin\\xcode\\repository\\aa.git"))) {

            List<Ref> call = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            for (Ref ref : call) {
                System.out.println("分支："+ref.getName().replace("refs/heads/",""));
            }
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }

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








































