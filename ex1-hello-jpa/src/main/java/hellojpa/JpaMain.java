package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        //엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유
        EntityManagerFactory enf = Persistence.createEntityManagerFactory("hello");
        //엔티티 매니저는 쓰레드간에 공유해선 안된다(쓰고 버려야 함)
        EntityManager em = enf.createEntityManager();
        //JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            //INSERT
            /*
            Member member = new Member();
            member.setId(2L);
            member.setName("HelloB");

            em.persist(member);

            tx.commit();
            */

            //SEARCH
            /*
            Member findMember = em.find(Member.class, 1L);
            System.out.println("findMember.id = "+findMember.getId());
            System.out.println("findMember.name = "+findMember.getName());
            */

            //REMOVE
            /*
            em.remove(findMember);
            */

            //UPDATE
            /*
            Member findMember = em.find(Member.class, 1L);
            findMember.setName("HelloJPA");
            // persist를 해주지 않아도 업데이트 된다.
            tx.commit();
            */

            //전체 검색
            //별칭이 필수 (Member를 m으로 지정)

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        enf.close();
    }

}
