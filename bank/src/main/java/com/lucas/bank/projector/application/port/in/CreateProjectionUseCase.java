package com.lucas.bank.projector.application.port.in;

import com.lucas.bank.shared.stream.StreamEventDTO;

public interface CreateProjectionUseCase {
    void project(StreamEventDTO event);
}
