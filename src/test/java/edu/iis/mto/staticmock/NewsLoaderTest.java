package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {
//ConfigurationLoader  NewsReaderFactory

    private NewsLoader newsLoader;
    private ConfigurationLoader configurationLoader;
    private NewsReader newsReader;
    private Configuration configuration;
    private IncomingNews incomingNews;

    @Before
    public void setUp() throws Exception {
        newsLoader = new NewsLoader();
        configuration = new Configuration();
        incomingNews = new IncomingNews();

        mockStatic(NewsReaderFactory.class);
        mockStatic(ConfigurationLoader.class);

        configurationLoader = mock(ConfigurationLoader.class);
        newsReader = mock(NewsReader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(ConfigurationLoader.getInstance().loadConfiguration()).thenReturn(configuration);
        Whitebox.setInternalState(configuration, "readerType", "readerType");
        when(NewsReaderFactory.getReader("readerType")).thenReturn(newsReader);
        when(newsReader.read()).thenReturn(incomingNews);
    }

    @Test
    public void testForNewsReaderAfterNewsLoader() {
        newsLoader.loadNews();
        verify(newsReader, times(1)).read();
    }

    @Test
    public void test100TimesForNewsReaderAfterNewsLoader() {
        int count = 100;
        for (int i = 0; i < count; i++) {
            newsLoader.loadNews();
        }
        verify(newsReader, times(count)).read();
    }

    @Test
    public void testPublishableNewsPublicContentType() {
        PublishableNews publishableNews = PublishableNews.create();
        publishableNews.addPublicInfo("test1");
        assertThat(((List<String>) Whitebox.getInternalState(publishableNews, "publicContent")).size(), is(equalTo(1)));
    }

    @Test
    public void testPublishableNewsFor100PublicContentT1ype() {
        PublishableNews publishableNews = PublishableNews.create();
        int count = 100;
        for (int i = 0; i < count; i++) {
            publishableNews.addPublicInfo("test" + i);
        }
        assertThat(((List<String>) Whitebox.getInternalState(publishableNews, "publicContent")).size(),
                is(equalTo(count)));
    }

    @Test
    public void testForPublishableNewsForSubscribentContent() {
        PublishableNewsMock publishableNewsMock = (PublishableNewsMock) PublishableNewsMock.create();
        publishableNewsMock.addForSubscription("test", null);

        assertThat(((List<String>) Whitebox.getInternalState(publishableNewsMock, "subscribentContent")).size(),
                is(equalTo(1)));
    }
}