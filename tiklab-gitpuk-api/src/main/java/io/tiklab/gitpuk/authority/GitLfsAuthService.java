package io.tiklab.gitpuk.authority;

import io.tiklab.gitpuk.authority.request.LfsData;

import java.io.IOException;

public interface GitLfsAuthService {

    void HandleLfsBatch(LfsData lfsData) throws IOException;
}
