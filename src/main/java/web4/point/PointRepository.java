package web4.point;

import web4.point.Point;
import web4.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM Point p WHERE p.user.id = :userId")
    void deleteByUserId(Long userId);
}
