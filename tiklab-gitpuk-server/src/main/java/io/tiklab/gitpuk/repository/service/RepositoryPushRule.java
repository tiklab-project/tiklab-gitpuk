package io.tiklab.gitpuk.repository.service;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.transport.ReceivePack;
import org.springframework.stereotype.Service;

/*
* 仓库推送规则
* */
@Service
public class RepositoryPushRule {


    //仓库规则校验
    public void pushRuleVerify(ReceivePack receivePack, ReceiveCommand receiveCommand, String requestURI){

    }
}
