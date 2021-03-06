package kr.co.fastcampus.eatgo.application;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import kr.co.fastcampus.eatgo.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class RestaurantServiceTest {

    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Before
    public void setUp(){

        MockitoAnnotations.initMocks(this);  //Mock붙어있는 것들 초기화 시켜준다

        mockRestaurantRepository();
        mockMenuItemRepository();
//        restaurantRepository = new RestaurantRepositoryImpl();
//        menuItemRepository = new MenuItemRepositoryImpl();

        restaurantService = new RestaurantService(
                restaurantRepository, menuItemRepository);
    }



    private void mockRestaurantRepository() {
        List<Restaurant> restaurants = new ArrayList<>();
        Restaurant restaurant = Restaurant.builder()
                .id(1004L)
                .name("Bob zip")
                .address("Seoul")

                .build();
        restaurants.add(restaurant);
        given(restaurantRepository.findAll()).willReturn(restaurants);

        given(restaurantRepository.findById(1004L)).willReturn(Optional.of(restaurant));


    }
    private void mockMenuItemRepository() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(MenuItem.builder()
                .name("Kimchi")
                .build());
        given(menuItemRepository.findAllByRestaurantId(1004L)).willReturn(menuItems);
    }
    @Test
    public void getRestaurants() {
        List<Restaurant> restaurants = restaurantService.getRestaurants();

        Restaurant restaurant = restaurants.get(0);
        assertThat(restaurant.getId(),is(1004L));
    }


    @Test
    public void getRestaurantWithExisted() {
        Restaurant restaurant =restaurantService.getRestaurant(1004L);

        assertThat(restaurant.getId(), is(1004L));

        MenuItem menuItem =restaurant.getMenuItems().get(0);

        assertThat(menuItem.getName(), is("Kimchi"));
    }
    @Test(expected = RestaurantNotFoundException.class)  //예외가 발생하는 걸 처리하기 위해서 이렇게 써야 한다.
    public void getRestaurantNotWithExisted() {
        restaurantService.getRestaurant(404L);
    }

    @Test
    public void addRestaurant(){
        given(restaurantRepository.save(any())).will(invocation -> {
            Restaurant restaurant = invocation.getArgument(0);
            restaurant.setId(1234L);
            return restaurant;
        });
        Restaurant restaurant = Restaurant.builder()
                .name("BeRyong")
                .address("Busan")
                .build();


        Restaurant created = restaurantService.addRestaurant(restaurant);


        assertThat(created.getId(), is(1234L));
    }
    @Test
    public void updateRestaurant(){
        Restaurant restaurant = Restaurant.builder()
                .id(1004L)
                .name("Bob zip")
                .address("Seoul")
                .build();



        given(restaurantRepository.findById(1004L))
                .willReturn(Optional.of(restaurant));
        restaurantService.updateRestaurant(1004L,"Sool zip","Busan");

        assertThat(restaurant.getName(),is("Sool zip"));
        assertThat(restaurant.getAddress(),is("Busan"));
    }
}

