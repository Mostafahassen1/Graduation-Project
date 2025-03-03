package com.codemeet.repository;



import com.codemeet.entity.Friendship;
import com.codemeet.utils.dto.FriendshipRequest;
import com.codemeet.utils.dto.FriendshipResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
   @Query("""
       SELECT new com.codemeet.utils.dto.FriendshipResponse(
           f.id,
           CASE
               WHEN f.from.id=:userId THEN f.to.firstName
               ELSE f.from.firstName
           END,
           CASE
                WHEN f.from.id=:userId THEN f.to.lastName
               ELSE f.from.lastName
           END,
           CASE
               WHEN f.from.id = :userId THEN f.to.username 
               ELSE f.from.username 
           END,
           CASE 
               WHEN f.from.id = :userId THEN f.to.profilePictureUrl 
               ELSE f.from.profilePictureUrl 
           END, 
           f.status
       ) 
       FROM Friendship f 
       WHERE (f.from.id = :userId OR f.to.id = :userId) 
       AND f.status = FriendshipStatus.ACCEPTED
       """)
   List<FriendshipResponse> getAllFriends(Integer userId);





   @Query(
           """
           SELECT f
           FROM Friendship f
           WHERE (f.from.id = :fromId AND f.to.id = :toId)
           OR (f.from.id = :toId AND f.to.id = :fromId)
           """
   )
   Optional<Friendship> findByFromIdAndToId(Integer fromId, Integer toId);
   @Query(
           """
           SELECT f
           FROM Friendship f
           WHERE f.from.id = :userId OR f.to.id = :userId
           ORDER BY f.status
           """
   )
   List<Friendship> findAllByUserId(Integer userId);

   @Query(
           """
           SELECT f
           FROM Friendship f
           WHERE (f.from.id = :userId OR f.to.id = :userId)
           AND f.status = "ACCEPTED"
           """
   )
   List<Friendship> findAllAcceptedByUserId(Integer userId);

   // Finds all friendship requests `sent by` user whose id is `userId`
   @Query(
           """
           SELECT f
           FROM Friendship f
           WHERE f.from.id = :userId
           AND f.status = "PENDING"
           """
   )
   List<Friendship> findAllPendingSentByUserId(Integer userId);

   // Finds all friendship requests `sent to` user whose id is `userId`
   @Query(
           """
           SELECT f
           FROM Friendship f
           WHERE f.to.id = :userId
           AND f.status = "PENDING"
           """
   )
   List<Friendship> findAllPendingReceivedByUserId(Integer userId);

   boolean existsByFromIdAndToId(Integer fromId, Integer toId);
}
