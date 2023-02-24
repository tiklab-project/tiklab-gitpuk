package net.tiklab.xcode.git;


import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;
import net.tiklab.core.exception.ApplicationException;
import net.tiklab.xcode.branch.model.CodeBranch;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.eclipse.jgit.api.DiffCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class GitTest {


    public static void main(String[] args) throws Exception {

        findAllBranch("C:\\Users\\admin\\xcode\\repository\\aa.git");

    }

    public static void findAllBranch(String repositoryAddress) throws IOException {

        Git git = Git.open(new File(repositoryAddress));
        Repository repository = git.getRepository();

        List<Ref> refs = repository.getRefDatabase().getRefs();

        String defaultBranch = " ";
        for (Ref ref : refs) {

            String name = ref.getName();
            if (name.equals("HEAD")) {
                Ref target = ref.getTarget();
                defaultBranch = target.getName();
                continue;
            }
            Ref.Storage storage = ref.getStorage();
            String name1 = storage.name();
            String Id = ref.getObjectId().getName();
            String s = name.replace("refs/heads/", "");

        }
        git.close();

    }























}








































