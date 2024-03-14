package wisteria.cvapp.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wisteria.cvapp.model.Cv;
import wisteria.cvapp.model.dto.CvDetailsDto;
import wisteria.cvapp.service.CvService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/cv")
@RequiredArgsConstructor
public class CvController {
    private final CvService cvService;

    //read cv list for user
    @GetMapping()
    public ResponseEntity<List<Cv>> getCvList(@RequestParam Integer userId) {
        return ResponseEntity.ok(this.cvService.getAllCvForUserId(userId));
    }

    //read cv for user
    @GetMapping(value = "/id/{id}")
    public ResponseEntity<CvDetailsDto> getCv(@PathVariable Integer id,
                                              @RequestParam Integer userId) {
        return ResponseEntity.ok(this.cvService.getCv(id, userId));
    }

    //create
    @PostMapping()
    public ResponseEntity<Integer> createCv(@RequestBody CvDetailsDto cvDetailsDto) {
        return ResponseEntity.ok(this.cvService.createCv(cvDetailsDto));
    }

    //update
    @PatchMapping(value = "/id/{id}")
    public ResponseEntity<Integer> updateCv(@PathVariable Integer id,
                                            @RequestBody CvDetailsDto cvDetailsDto) {
        return ResponseEntity.ok(this.cvService.updateCv(id, cvDetailsDto));
    }
    //delete
    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<Integer> deleteCv(@PathVariable Integer id){
        return ResponseEntity.ok(this.cvService.deleteCv(id));
    }

    //download

}
