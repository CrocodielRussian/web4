package web4.point;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import web4.point.Point;
import web4.point.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/points")
public class PointController {
    @Autowired
    private PointService pointService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Point> addPoint(@RequestBody PointRequest request) {
        try {
            Point point = pointService.addPoint(request.getX(), request.getY(), request.getR());
            return ResponseEntity.ok(point);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Point>> getPoints() {
        return ResponseEntity.ok(pointService.getPointsForUser());
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> clearPoints() {
        pointService.clearPointsForUser();
        return ResponseEntity.ok().build();
    }
}

@Setter
@Getter
class PointRequest {
    private double x;
    private double y;
    private double r;

}