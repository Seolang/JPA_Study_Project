import jpql.Member;

import javax.persistence.*;

public class JpaSubQuery {

    public static void main(String[] args) {

        EntityManagerFactory enf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = enf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            // JPA 서브쿼리는 SELECT절, WHERE절과 HAVING절에서만 사용가능(FROM 에서 불가능)

            // EXAMPLE
            String query = "select m from Member m where exists (select t from m.team t where t.name = 'teamA')";
            //String query = "select o from Order o where o.orderAmount > ALL (select p.stockAmount from Product p)";
            //String query = "select m from Member m where m.team = ANY (select t from Team t)";



            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        enf.close();
    }
}
