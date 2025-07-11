package br.ufscar.dc.dsw.AA2.controllers;

import br.ufscar.dc.dsw.AA2.dtos.testSession.*;
import br.ufscar.dc.dsw.AA2.services.TestSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/test-sessions")
public class TestSessionController {
    @Autowired
    private TestSessionService testSessionService;

    @PostMapping("/{projectId}")
    public ResponseEntity<GetTestSessionResponseDTO> create(@PathVariable("projectId") UUID projectId, @RequestBody CreateTestSessionRequestDTO request) {
        GetTestSessionResponseDTO session = testSessionService.createTestSession(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @GetMapping
    public ResponseEntity<List<GetTestSessionResponseDTO>> getAllAllowed(@RequestHeader("Authorization") String token) {
        List<GetTestSessionResponseDTO> sessions = testSessionService.getAllowedTestSessionsByToken(token);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetTestSessionResponseDTO> getById(@PathVariable UUID id) {
        GetTestSessionResponseDTO session = testSessionService.getTestSessionById(id);
        return ResponseEntity.ok(session);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetTestSessionResponseDTO> update(@RequestHeader("Authorization") String token, @PathVariable UUID id,
                                                                       @RequestBody UpdateSessionRequestDTO request) {
        GetTestSessionResponseDTO session = testSessionService.updateSession(token, id, request);
        return ResponseEntity.ok(session);
    }

    @PatchMapping("{id}/status")
    public ResponseEntity<String> updateStatus(@RequestHeader("Authorization") String token, @PathVariable UUID id) {
        GetTestSessionResponseDTO session = testSessionService.updateSessionStatus(token, id);
        return ResponseEntity.ok("Status da sessão atualizado para: " + session.getStatus());
    }

    @PatchMapping("{id}/add-bug")
    public ResponseEntity<AddTestSessionBugResponseDTO> addBug(@RequestHeader("Authorization") String token, @PathVariable UUID id, @RequestBody AddTestSessionBugRequestDTO request) {
        AddTestSessionBugResponseDTO response = testSessionService.addTestSessionBugs(token, id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("Authorization") String token, @PathVariable UUID id) {
        testSessionService.deleteTestSession(token, id);
        return ResponseEntity.noContent().build();
    }
}
