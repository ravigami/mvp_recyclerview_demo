package com.uday.mvp_recyclerview_demo;

import com.google.gson.Gson;
import com.uday.mvp_recyclerview_demo.model.Country;
import com.uday.mvp_recyclerview_demo.network.NetworkService;
import com.uday.mvp_recyclerview_demo.presenter.MainPresenter;
import com.uday.mvp_recyclerview_demo.presenter.MainViewInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class JsonResponseLoaderTest {
    private ClassLoader mClassLoader;
    @Mock
    private NetworkService networkService;
    @Mock
    private MainViewInterface mainViewInterface;

    private MainPresenter mainPresenter;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mainPresenter =  new MainPresenter(mainViewInterface,networkService);
        mClassLoader = this.getClass().getClassLoader();
        mainPresenter = new MainPresenter(mainViewInterface, networkService );

    }
    @Test
    public void testJsonFileLoading() {
        // Given


        // When
        String loadedJsonString = TestResourceReaderUtil.readFile(mClassLoader,
                "get_facts_sample_client_response.json");

        // Then
        assertThat(loadedJsonString, instanceOf(String.class));
    }

    @Test
    public void testJsonResponseSuccess() throws Exception {


        // Given

        String loadedJsonString = TestResourceReaderUtil.readFile(mClassLoader,
                "get_facts_sample_client_response.json");
        // when
        Observable<Country> countryObservable = Observable.just(new Gson().fromJson(loadedJsonString, Country.class));
        Mockito.when(networkService.getAPI().getCountryFacts()).thenReturn(countryObservable);
        mainPresenter.getFacts();

        //verify

        Mockito.verify(mainViewInterface).hideProgressBar();
        Mockito.verify(mainViewInterface).displayFacts(Mockito.any(Country.class));
    }

    @Test
    public void testJsonResponseFailure() throws Exception {

        // when
        Mockito.when(networkService.getAPI().getCountryFacts()).thenReturn(Observable.<Country>error(new Throwable()));
        mainPresenter.getFacts();
        //verify
        Mockito.verify(mainViewInterface).hideProgressBar();
        Mockito.verify(mainViewInterface).displayError(Mockito.any(String.class));
    }


}
