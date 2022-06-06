import jpql.Member;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaBulkQuery {
    public static void main(String[] args) {

        EntityManagerFactory enf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = enf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setAge(10);
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setAge(10);
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setAge(10);
            member3.setTeam(teamB);
            em.persist(member3);


            // 회원의 age 값이 한꺼번에 변경됨
            // 쿼리 전달시 flush 자동 실행
            int resultCount = em.createQuery("update Member m set m.age = 20").executeUpdate();
            
            // 주의점
            // 벌크연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리
            
            // 해결방법 
            // 1. 벌크 연산을 먼저 실행
            // 2. 벌크 연산 수행 후 영속성 컨텍스트 초기화


            System.out.println("resultCount = " + resultCount);

            // DB 변경 내용이 반영되어있지 않음
            System.out.println("member age = " + member1.getAge());

            // 조회를 시행해도 영속성 컨텍스트에서 가져오기 때문에 업데이트가 반영되지 않음
            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("member age = " + findMember.getAge());

            // 따라서 영속성 컨텍스트를 초기화 한 후 조회해야 함
            em.clear();
            Member findMember2 = em.find(Member.class, member1.getId());
            System.out.println("member age = " + findMember2.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        enf.close();
    }
    

}
