package org.srlab.usask.editexweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srlab.usask.editexweb.dto.EditTextDTO;
import org.srlab.usask.editexweb.dto.ResultDTO;
import org.srlab.usask.editexweb.service.EditExService;

/**
 * @author Sadman
 */
@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class EditExController {

    @Autowired
    EditExService editExService;

//    @Autowired
//    AttributeDetectionService attributeDetectionService;

    @PostMapping("/edit")
    public ResponseEntity<ResultDTO> editAnalysis(@RequestBody EditTextDTO editTextDTO) {
        return ResponseEntity.ok(editExService.detect(editTextDTO));
    }
}
