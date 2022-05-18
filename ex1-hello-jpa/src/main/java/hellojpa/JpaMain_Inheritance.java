package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain_Inheritance {
    public static void main(String[] args) {
        //엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유
        EntityManagerFactory enf = Persistence.createEntityManagerFactory("hello");
        //엔티티 매니저는 쓰레드간에 공유해선 안된다(쓰고 버려야 함)
        EntityManager em = enf.createEntityManager();
        //JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            Movie movie = new Movie();
            movie.setDirector("aaaa");
            movie.setActor("bbbb");
            movie.setName("바람과 함께 사라지다");
            movie.setPrice(10000);

            em.persist(movie);

            em.flush();
            em.clear();

            Movie findMovie = em.find(Movie.class, movie.getId());
            // Item findMovie = em.find(Item.class, movie.getId());  Table Per Class 전략에서 부모 클래스로 find시 모든 테이블을 Union 하여 다 뒤져봐야함
            //                                                       이는 색인 효율이 떨이짐

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        enf.close();
    }
}
