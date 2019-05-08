package lab3_2;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

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

public class ConfigurationLoaderTest1 {

    public NewsReader newsReader;
    public NewsLoader newsLoader;
    public IncomingNews incomingNews;
    public Configuration configuration;
    public ConfigurationLoader configurationLoader;

    @Before
    public void setUp() {
        incomingNews = new IncomingNews();
        configuration = new Configuration();
        newsLoader = new NewsLoader();

        configurationLoader = mock(ConfigurationLoader.class);
        mockStatic(ConfigurationLoader.class);
        mockStatic(NewsReaderFactory.class);

        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);

        when(ConfigurationLoader.getInstance()
                                .loadConfiguration()).thenReturn(configuration);

        Whitebox.setInternalState(configuration, "readerType", "Any");

        newsReader = mock(NewsReader.class);
        when(NewsReaderFactory.getReader("Any")).thenReturn(newsReader);

        when(newsReader.read()).thenReturn(incomingNews);
    }

    @Test
    public void isLoadConfigurationOnes() {
        incomingNews.add(new IncomingInfo("NewNewsA", SubsciptionType.A));
        incomingNews.add(new IncomingInfo("NewNewsB", SubsciptionType.B));
        incomingNews.add(new IncomingInfo("NewNewsC", SubsciptionType.C));
        incomingNews.add(new IncomingInfo("NewsNONE", SubsciptionType.NONE));

        newsLoader.loadNews();

        verify(configurationLoader, times(1)).loadConfiguration();
    }

    class PublishableNewsTest extends PublishableNews {

        public List<String> publicContent = new ArrayList<>();
        public List<String> subscribentContent = new ArrayList<>();

        @Override
        public void addPublicInfo(String content) {
            this.publicContent.add(content);
        }

        @Override
        public void addForSubscription(String content, SubsciptionType subscriptionType) {
            this.subscribentContent.add(content);
        }
    }

    @Test
    public void isReturnTwoPublicNews() {
        incomingNews.add(new IncomingInfo("NewNewsA", SubsciptionType.A));
        incomingNews.add(new IncomingInfo("NewNewsB", SubsciptionType.B));
        incomingNews.add(new IncomingInfo("NewNewsC", SubsciptionType.C));
        incomingNews.add(new IncomingInfo("NewNewsNONE1", SubsciptionType.NONE));
        incomingNews.add(new IncomingInfo("NewNewsNONE2", SubsciptionType.NONE));

        mockStatic(PublishableNews.class);

        when(PublishableNews.create()).thenReturn(new PublishableNewsTest());

        PublishableNewsTest publishNewsTest = (PublishableNewsTest) newsLoader.loadNews();

        assertThat(publishNewsTest.publicContent.get(0), is(equalTo("NewNewsNONE1")));
        assertThat(publishNewsTest.publicContent.get(1), is(equalTo("NewNewsNONE2")));
        assertThat(publishNewsTest.publicContent.size(), is(equalTo(2)));
    }

    @Test
    public void isReturnThreePublicNews() {
        incomingNews.add(new IncomingInfo("NewNewsNONE1", SubsciptionType.NONE));
        incomingNews.add(new IncomingInfo("NewNewsNONE2", SubsciptionType.NONE));
        incomingNews.add(new IncomingInfo("NewNewsC", SubsciptionType.C));
        incomingNews.add(new IncomingInfo("NewNewsB", SubsciptionType.B));
        incomingNews.add(new IncomingInfo("NewNewsA", SubsciptionType.A));

        mockStatic(PublishableNews.class);

        when(PublishableNews.create()).thenReturn(new PublishableNewsTest());

        PublishableNewsTest publishNewsTest = (PublishableNewsTest) newsLoader.loadNews();
        assertThat(publishNewsTest.subscribentContent.size(), is(equalTo(3)));
        assertThat(publishNewsTest.subscribentContent.get(0), is(equalTo("NewNewsC")));
        assertThat(publishNewsTest.subscribentContent.get(1), is(equalTo("NewNewsB")));
        assertThat(publishNewsTest.subscribentContent.get(2), is(equalTo("NewNewsA")));

    }

}
