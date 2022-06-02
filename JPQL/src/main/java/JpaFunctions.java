import jpql.Member;
import jpql.MemberType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaFunctions {

    public static void main(String[] args) {

        EntityManagerFactory enf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = enf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {


            Member member = new Member();
            member.setUsername("member");
            member.setAge(10);
            member.setType(MemberType.ADMIN);

            em.persist(member);

            em.flush();
            em.clear();
            // Case
            String caseQuery =
                    "select " +
                        "case " +
                            "when m.age <= 10 then '학생요금' " +
                            "when m.age >= 60 then '경로요금' " +
                            "else '일반요금' " +
                        "end " +
                    "from Member m";

            // COALESCE : 하나씩 조회해서 null이 아니면 반환
            String coalesceQuery = "select coalesce(m.username,'이름 없는 회원') as username from Member m";

            // NULLIF : 두 값이 같으면 null 반환, 다르면 첫번째 값 반환
            String nullIfQuery = "select NULLIF(m.username, '관리자') from Member m";

            // CONCAT
            String concatQuery = "select CONCAT('a', 'b') from Member m";

            // SUBSTRING
            String subStringQuery = "select SUBSTRING(m.username, 2, 3) from Member m";

            // LOCATE : 위치
            String locateQuery = "select LOCATE('de', 'abcdefg') from Member m";

            // SIZE : DB collection의 크기 리턴
            String sizeQuery = "select SIZE(t.members) from Team t";


            List<String> result = em.createQuery(subStringQuery, String.class).getResultList();
            List<Integer> result2 = em.createQuery(locateQuery, Integer.class).getResultList();

            for(Integer s : result2) {
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
