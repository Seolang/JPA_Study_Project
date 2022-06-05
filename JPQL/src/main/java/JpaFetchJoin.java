import jpql.Member;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaFetchJoin {
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



            em.flush();
            em.clear();

            String query = "select m from Member m join fetch m.team";

            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            for(Member member : result) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
                // join fetch 없이 회원만 가져왔을 때
                // 회원 1, 팀A(SQL)
                // 회원 2, 팀A(1차캐시)
                // 회원 3, 팀B(SQL)

                // 회원 100명 -> 최악의 경우 : N(Team을 가져오는 쿼리) + 1(회원을 가져오는 쿼리)
                
                // join fetch 사용시
                // join으로 한번에 다 가져옴 (지연로딩이여도 프록시가 아닌 team 값이 직접 들어감) -> 실무에서 많이 사용
            }


            // one to many 에 대한 fetch join
            String query2 = "select distinct t from Team t join fetch t.members";

            List<Team> result2 = em.createQuery(query2, Team.class)
                    .getResultList();

            for(Team team : result2) {
                System.out.println("team = " + team.getName() + ", " + team.getMembers().size());
                // join fetch로 인해 team 출력 수가 members의 수와 같다. (데이터가 뻥튀기 된다, Many to One에서는 발생하지 않음)
                // distinct를 쓰면? -> SQL 시점에서는 같은 team에 대해 join된 member가 달라 중복이 제거되지 않는다.
                // JPA에서 엔티티에 대한 중복 제거 시도를 추가적으로 시행하여 중복을 제거한다.
            }

            // 일반 join은 실행할 때 연관 엔티티를 한번에 조회하지 않지만(지연로딩), fetch join은 함께 조회한다(즉시로딩).
            // N + 1 문제를 해결할 수 있음

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        enf.close();
    }
    

}
