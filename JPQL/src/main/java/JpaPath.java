import jpql.Member;
import jpql.MemberType;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.List;

public class JpaPath {

    public static void main(String[] args) {

        EntityManagerFactory enf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = enf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team team = new Team();
            team.setName("teamA");

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setTeam(team);

            em.persist(member);
            em.persist(team);

            member.setUsername("changedName");

            em.flush();
            em.clear();

            // 상태필드 : 경로 탐색의 끝, 탐색 X (m.username)
            String query = "select m from Member m";
            // 단일 값 연관 경로 : 묵시적 Inner Join 발생, 탐색 O (m.team : 연관 경로, m.team.name : 상태 필드)
            String query2 = "select m.team from Member m";
            // 컬렉션 값 연관 경로 : 묵시적 내부 조인 발생, 탐색 X => members를 탐색할 수 없음, 따라서 명시적 join 사용으로 얻은 별칭이 필요하다.
            //String query3 = "select t.members from Team t";
            String query3 = "select m from Team t join t.members m";
            
            // 결론 : 묵시적 join을 쓰지 말고 명시적 Join 을 사용하자

            List<Member> resultList = em.createQuery(query3, Member.class).getResultList();

            for(Object s : resultList) {
                System.out.println("s = " + s);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        enf.close();
    }
}
