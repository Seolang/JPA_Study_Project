import jpql.Member;

import javax.persistence.*;

public class JpaQuery {

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

            //TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class); // Type 정보를 알 수 있을 때
            //TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
            //Query query3 = em.createQuery("select m.username, m.age from Member m", String.class); // Type정보를 알 수 없을 때


            TypedQuery<Member> query = em.createQuery("select m from Member m where m.username = :username", Member.class);
            query.setParameter("username", "member1");

            //List<Member> resultList = query.getResultList(); // 반환 값이 여러개 일때
            Member result = query.getSingleResult(); // 반환 값이 하나 일때
            // Spring Data JPA -> 예외처리를 spring이 해줌
            System.out.println("result = " + result.getUsername());

            Member singleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
                            .setParameter("username", "member1")
                            .getSingleResult();

            System.out.println("result = " + singleResult.getUsername());


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        enf.close();
    }

}
