package alchemystar.freedom.test.bptest;

import org.junit.Test;

import alchemystar.freedom.config.SystemConfig;
import alchemystar.freedom.meta.IndexEntry;
import alchemystar.freedom.meta.value.Value;
import alchemystar.freedom.meta.value.ValueBoolean;
import alchemystar.freedom.meta.value.ValueInt;
import alchemystar.freedom.meta.value.ValueLong;
import alchemystar.freedom.meta.value.ValueString;
import alchemystar.freedom.store.fs.FStore;
import alchemystar.freedom.store.item.Item;
import alchemystar.freedom.store.page.Page;
import alchemystar.freedom.store.page.PageLoader;
import alchemystar.freedom.store.page.PagePool;

/**
 * PageTest
 *
 * @Author lizhuyang
 */
public class PageTest {

    @Test
    public void pageTest() {
        Value[] values = new Value[5];
        values[0] = new ValueString("this is freedom db");
        values[1] = new ValueString("just enjoy it");
        values[2] = new ValueBoolean(true);
        values[3] = new ValueInt(5);
        values[4] = new ValueLong(6L);
        IndexEntry indexEntry = new IndexEntry(values);
        Item item = new Item(indexEntry);
        System.out.println(item.getLength());
        PagePool pagePool = PagePool.getIntance();
        Page page = pagePool.getFreePage();
        for (int i = 0; i < 1000; i++) {
            if (page.writeItem(item)) {
                continue;
            } else {
                System.out.println("btee=" + i + ",page size exhaust");
                break;
            }
        }
        FStore fStore = new FStore(SystemConfig.FREEDOM_REL_DATA_PATH);
        fStore.open();
        fStore.writePageToFile(page, 0);
        fStore.writePageToFile(page, 10);

        PageLoader loader = fStore.readPageLoaderFromFile(0);
        IndexEntry[] indexEntries = loader.getIndexEntries();
        for (int i = 0; i < indexEntries.length; i++) {
            System.out.println(indexEntries[i]);
        }
    }
}
