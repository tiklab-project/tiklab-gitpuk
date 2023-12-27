package io.thoughtware.gittok.repository.service;

import org.springframework.stereotype.Service;

@Service
public class MemoryManServiceImpl implements MemoryManService {


    @Override
    public boolean findResMemory() {
        return true;
    }
}
