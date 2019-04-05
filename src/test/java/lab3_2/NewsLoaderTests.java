package lab3_2;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.iis.mto.staticmock.Configuration;
import edu.iis.mto.staticmock.ConfigurationLoader;
import edu.iis.mto.staticmock.IncomingInfo;
import edu.iis.mto.staticmock.IncomingNews;
import edu.iis.mto.staticmock.NewsLoader;
import edu.iis.mto.staticmock.NewsReaderFactory;
import edu.iis.mto.staticmock.PublishableNews;
import edu.iis.mto.staticmock.SubsciptionType;
import edu.iis.mto.staticmock.reader.NewsReader;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class, PublishableNews.class})
public class NewsLoaderTests {

    private NewsLoader newsLoader;
    private NewsReader newsReaderMock;
    private Configuration configuration;
    private ConfigurationLoader configurationLoaderMock;
    private IncomingNews incomingNews;

    @Before
    public void setUp() {
        newsLoader = new NewsLoader();
        configuration = new Configuration();
        mockStatic(ConfigurationLoader.class);
        mockStatic(NewsReaderFactory.class);
        configurationLoaderMock = mock(ConfigurationLoader.class);
        incomingNews = new IncomingNews();
        incomingNews.add(new IncomingInfo("publiczny", SubsciptionType.NONE));
        incomingNews.add(new IncomingInfo("subA", SubsciptionType.A));
        incomingNews.add(new IncomingInfo("subB", SubsciptionType.B));
        incomingNews.add(new IncomingInfo("subC", SubsciptionType.C));
        newsReaderMock = mock(NewsReader.class);
        String readerTypeValue = "readerType";
        Whitebox.setInternalState(configuration, "readerType", readerTypeValue);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoaderMock);
        when(ConfigurationLoader.getInstance()
                                .loadConfiguration()).thenReturn(configuration);
        when(NewsReaderFactory.getReader(readerTypeValue)).thenReturn(newsReaderMock);
        when(newsReaderMock.read()).thenReturn(incomingNews);

    }

    @Test
    public void shouldCallReadMethodOnce() {
        newsLoader.loadNews();
        verify(newsReaderMock, times(1)).read();
    }

    @Test
    public void shouldReturnTwoItemsAddedToPublicList() {
        PublishableNews publishNews = PublishableNews.create();
        publishNews.addPublicInfo("public1");
        publishNews.addPublicInfo("public2");
        List<String> list = (List<String>) Whitebox.getInternalState(publishNews, "publicContent");
        assertThat(list.size(), is(equalTo(2)));
        assertThat(list.get(0), is(equalTo("public1")));
        assertThat(list.get(1), is(equalTo("public2")));
    }

    @Test
    public void shouldReturnProperAmountOfSubscribentContent() {

        final PublishableNews publishableNews = getMockOfPublishableNews();
        mockStatic(PublishableNews.class);
        when(PublishableNews.create()).thenReturn(publishableNews);
        PublishableNews publishable = newsLoader.loadNews();
        List<String> result = (List<String>) Whitebox.getInternalState(publishable, "subscribentContent");
        assertThat(result.size(), is(3));
        assertThat(result.get(0), is(equalTo("subA")));
    }

    private PublishableNews getMockOfPublishableNews() {
        return new PublishableNews() {

            private final List<String> subscribentContent = new ArrayList<>();

            @Override
            public void addForSubscription(String content, SubsciptionType subscriptionType) {
                this.subscribentContent.add(content);
            }
        };
    }

}
