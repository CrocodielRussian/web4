package web4.point;

import web4.point.Point;
import web4.user.User;
import web4.user.UserRepository;
import web4.point.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PointService {
    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Point addPoint(double x, double y, double r) {
        User user = getCurrentUser();
        if (!isValidInput(x, y, r)) {
            throw new IllegalArgumentException("Invalid input: X and R must be in [-5, 3], Y in [-5, 5], R > 0");
        }

        System.out.println("Principal: " + user);
        Point point = new Point();
        point.setX(x);
        point.setY(y);
        point.setR(r);
        point.setHit(isInArea(x, y, r));
        point.setTime(LocalDateTime.now());
        point.setUser(user);

        return pointRepository.save(point);
    }

    public List<Point> getPointsForUser() {
        User user = getCurrentUser();

        return pointRepository.findByUserId(user.getId());
    }

    @Transactional
    public void clearPointsForUser() {
        User user = getCurrentUser();
        pointRepository.deleteByUserId(user.getId());
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }



    private boolean isValidInput(double x, double y, double r) {
        return x >= -5 && x <= 3 && y >= -5 && y <= 5 && r >= -5 && r <= 3 && r > 0;
    }

    private boolean isInArea(double x, double y, double r) {
        if ((x <= 0 && y >= 0)
                && (x >= -r && x + y <= r)){
            return true;
        }

        if((x >= 0 && y <= 0)
                && (x*x + y*y <= r*r)){
            return true;
        }

        if((x <= 0 && y <= 0)
                && (x >= -r)
                && (y >= -r/2)){
            return true;
        }

        return false;
    }
}