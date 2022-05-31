import jpql.Address;
import jpql.Member;
import jpql.MemberDTO;
import jpql.Team;

import javax.persistence.*;
import java.util.List;

public class JpaProjection {

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

            em.flush();
            em.clear();

            List<Team> result = em.createQuery("select t from Member m join m.team t", Team.class)  // 엔티티 프로젝션
                    .getResultList();

            List<Address> result2 = em.createQuery("select o.address from Order o", Address.class)  // 임베디드 타입 프로젝션
                    .getResultList();

            // 스칼라 다중 조회 방법
            /*
            // 1. Query 타입으로 조회
            List resultList = em.createQuery("select distinct m.username, m.age from Member m")  // 스칼라 타입 프로젝션
                    .getResultList();

            Object o = resultList.get(0);
            Object[] result3 = (Object[]) o;
            */

            /*
            // 2. Object[] 타입으로 조회
            List<Object[]> resultList = em.createQuery("select distinct m.username, m.age from Member m")  // 스칼라 타입 프로젝션
                    .getResultList();
            Object[] result3 = resultList.get(0);

            System.out.println("username = " + result3[0]);
            System.out.println("age = " + result3[1]);
            */

            //3. new 명령어로 조회 (추천)
            List<MemberDTO> resultList = em.createQuery("select distinct new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)  // 스칼라 타입 프로젝션
                    .getResultList();
            MemberDTO memberDTO = resultList.get(0);
            System.out.println("username = " + memberDTO.getUsername());
            System.out.println("age = " + memberDTO.getAge());


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        enf.close();
    }

}
