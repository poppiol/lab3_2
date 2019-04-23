package lab3_2;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

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
public class NewsLoaderTest {

    private NewsLoader newsLoader;
    private NewsReader newsReader;
    private Configuration configuration;
    private ConfigurationLoader configurationLoader;
    private IncomingNews incomingNews;

    @Before
    public void start() {
        newsLoader = new NewsLoader();
        configuration = new Configuration();
        incomingNews = new IncomingNews();

        mockStatic(ConfigurationLoader.class);
        mockStatic(NewsReaderFactory.class);
        configurationLoader = mock(ConfigurationLoader.class);

        newsReader = mock(NewsReader.class);
        Whitebox.setInternalState(configuration, "readerType", "Any");
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(ConfigurationLoader.getInstance()
                                .loadConfiguration()).thenReturn(configuration);
        when(NewsReaderFactory.getReader("Any")).thenReturn(newsReader);
        when(newsReader.read()).thenReturn(incomingNews);
    }

    @Test
    public void shouldCallReadMethodOnceWhenLoadingNews() {
        incomingNews.add(new IncomingInfo("subA", SubsciptionType.A));
        incomingNews.add(new IncomingInfo("subB", SubsciptionType.B));
        incomingNews.add(new IncomingInfo("subN", SubsciptionType.NONE));

        newsLoader.loadNews();
        verify(newsReader, times(1)).read();

    }

    @Test
    public void shouldReturnProperAmountOfItemsAddedToPublicList() {
        PublishableNews publishNews = PublishableNews.create();
        publishNews.addPublicInfo("info1");
        publishNews.addPublicInfo("info2");
        publishNews.addPublicInfo("info3");
        publishNews.addPublicInfo("info4");

        List<String> content = (List<String>) Whitebox.getInternalState(publishNews, "publicContent");
        assertThat(content.size(), equalTo(4));

    }

}
