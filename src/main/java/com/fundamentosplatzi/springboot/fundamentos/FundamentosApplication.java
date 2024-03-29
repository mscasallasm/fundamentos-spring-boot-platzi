package com.fundamentosplatzi.springboot.fundamentos;

import antlr.collections.impl.LList;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBean;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentosplatzi.springboot.fundamentos.component.ComponentDependency;
import com.fundamentosplatzi.springboot.fundamentos.entity.User;
import com.fundamentosplatzi.springboot.fundamentos.pojo.UserPojo;
import com.fundamentosplatzi.springboot.fundamentos.repository.UserRepository;
import com.fundamentosplatzi.springboot.fundamentos.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	private final Log LOGGER = LogFactory.getLog(FundamentosApplication.class);

	private ComponentDependency componentDependency;
	private MyBean myBean;

	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithProperties myBeanWithProperties;
	private UserPojo userPojo;
	private UserRepository userRepository;
	private UserService userService;


	public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency, MyBeanWithProperties myBeanWithProperties, UserPojo userPojo, UserRepository userRepository, UserService userService){
		this.componentDependency = componentDependency;
		this.myBean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
		this.userService = userService;
	}


	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//ejemplosAnteriores();
		saveUserInDataBase();
		getInformationJpqlFromUser();
		saveWithErrorTransactional();
	}

	private void saveWithErrorTransactional(){
		User test1 = new User("test1","test1@mail.com", LocalDate.now());
		User test2 = new User("test2","test2@mail.com", LocalDate.now());
		User test3 = new User("test3","test1@mail.com", LocalDate.now());
		User test4 = new User("test4","test4@mail.com", LocalDate.now());

		List<User> users = Arrays.asList(test1, test2, test3, test4);

		try {
			userService.saveTransactional(users);
		} catch (Exception e) {
			LOGGER.error("Esta es una exception dentro del metodo transaccional " + e);
		}

		userService.getAllUsers().stream()
				.forEach(user ->
						LOGGER.info("Este es el usuario dentro del metodo transaccional " + user));

	}

	private void getInformationJpqlFromUser() {
		/*LOGGER.info("Usuario con el metodo findByUserEmail" +
				userRepository.findByUserEmail("maik@mail.com")
						.orElseThrow(() -> new RuntimeException("No se encontro el usuario")));

		userRepository.findAndSort("user", Sort.by("id").descending())
				.stream()
				.forEach(user -> LOGGER.info("Usuario con metodo sort " + user));

		userRepository.findByName("Maik")
				.stream()
				.forEach(user -> LOGGER.info("Usuario con query method " + user));

		LOGGER.info( "Usuario con query method findByEmailAndName" + userRepository.findByEmailAndName("user3@mail.com","user3")
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado")));

		userRepository.findByNameLike("%user%")
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByNameLike " + user ));

		userRepository.findByNameOrEmail("user4", null)
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByNameOrEmail " + user ));*/

		userRepository
				.findByBirthDateBetween(LocalDate.of(2021,03,1),LocalDate.of(2021,10,30))
				.stream()
				.forEach(user -> LOGGER.info("Usuario con intervalo de fechas: " + user));

		userRepository.findByNameContainingOrderByIdDesc("user")
				.stream()
				.forEach(user -> LOGGER.info("Usuario encontrado con like y ordenado " + user));

		LOGGER.info("El usuario a partir del named parameter es: " + userRepository.getAllByBirthDateAndEmail(LocalDate.of(2021,8,3),"user3@mail.com")
				.orElseThrow(() -> new RuntimeException("No se encontro el usuario a partir del named parameter")));
	}

	public void saveUserInDataBase() {
		User user1 = new User("Maik","maik@mail.com", LocalDate.of(2021,12,30));
		User user2 = new User("Maik","user2@mail.com", LocalDate.of(2021,11,1));
		User user3 = new User("user3","user3@mail.com", LocalDate.of(2021,8,3));
		User user4 = new User("user4","user4@mail.com", LocalDate.of(2021,5,15));
		User user5 = new User("user5","user5@mail.com", LocalDate.of(2021,3,18));
		User user6 = new User("user6","user6@mail.com", LocalDate.of(2021,4,1));
		User user7 = new User("user7","user7@mail.com", LocalDate.of(2021,5,14));
		User user8 = new User("user8","user8@mail.com", LocalDate.of(2021,7,25));
		User user9 = new User("user9","user9@mail.com", LocalDate.of(2021,1,23));
		User user10 = new User("user10","user10@mail.com", LocalDate.of(2021,2,28));
		User user11 = new User("user11","user11@mail.com", LocalDate.of(2021,9,5));
		User user12 = new User("user12","user12@mail.com", LocalDate.of(2021,6,8));
		List<User> list = Arrays.asList(user1,user2,user3,user4,user5,user6,user7,user8,user9,user10,user11,user12);

		list.stream().forEach(userRepository::save);

	}

	private void ejemplosAnteriores(){
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail() + "-" + userPojo.getPassword() + "-" + userPojo.getAge());
		try {
			int value = 10 / 0;
			LOGGER.debug("Mi valor :" + value);
		} catch (Exception e) {
			LOGGER.error("Esto es un error al dividir por cero " + e.getMessage());
		}
	}
}
