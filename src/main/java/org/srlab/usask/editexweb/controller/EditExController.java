package org.srlab.usask.editexweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srlab.usask.editexweb.dto.EditTextDTO;
import org.srlab.usask.editexweb.dto.ResultDTO;
import org.srlab.usask.editexweb.service.EditExService;
import org.srlab.usask.editexweb.service.IEditService;

/**
 * @author Sadman
 */
@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class EditExController {

    @Autowired
    EditExService editExService;

    @Autowired
    IEditService iEditService;

//    @Autowired
//    AttributeDetectionService attributeDetectionService;

    @PostMapping("/editex")
    public ResponseEntity<ResultDTO> editExAnalysis(@RequestBody EditTextDTO editTextDTO) {
        return ResponseEntity.ok(editExService.detect(editTextDTO));
    }

    @PostMapping("/edit")
    public ResponseEntity<ResultDTO> iEditAnalysis(@RequestBody EditTextDTO editTextDTO) {
        return ResponseEntity.ok(iEditService.detect(editTextDTO));
    }
}
