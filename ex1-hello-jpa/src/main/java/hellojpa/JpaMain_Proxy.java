package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain_Proxy {
    public static void main(String[] args) {
        //엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유
        EntityManagerFactory enf = Persistence.createEntityManagerFactory("hello");
        //엔티티 매니저는 쓰레드간에 공유해선 안된다(쓰고 버려야 함)
        EntityManager em = enf.createEntityManager();
        //JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            Member member = em.find(Member.class, 1L);
            printMemberAndTeam(member);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        enf.close();
    }

    private static void printMemberAndTeam(Member member) {

    }
}
