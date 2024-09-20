import com.codeborne.selenide.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;


public class WildberriesTests {

    @BeforeAll
    static void setUp() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
    }

    @AfterAll
    static void tearDown() {
        closeWebDriver();
    }

    @Test
    @DisplayName("Работа с поисковой строкой")
    public void inputSearchTest() {
        Selenide.open("https://www.wildberries.ru");

        $("#searchInput").click();
        $("#searchInput").setValue("Iphone 13").pressEnter();
        $(".searching-results__title").shouldHave(Condition.text("Iphone 13"));
        $(".filter-switch .btn-switch__text")
                .shouldHave(Condition.text("Ухтыдажа"));
        $(".dropdown-filter__btn.dropdown-filter__btn--sorter")
                .shouldHave(Condition.text("По популярности"));
        $(byClassName("product-card__link")).shouldBe(visible);
        $(".product-card__link").closest("article").find(".product-card__brand")
                .shouldHave(Condition.text("Apple"));
        $(byClassName("search-catalog__btn--clear")).click();
        $("#searchInput").shouldBe(Condition.empty);
    }

    @Test
    @DisplayName("Смена города")
    void changeCity() {
        Selenide.open("https://www.wildberries.ru");
        $(("span[data-wba-header-name='DLV_Adress']")).click();
        $("input[placeholder='Введите адрес']").setValue("Санкт-Петербург").pressEnter();
        sleep(1000);
        $(".address-item__name-text span").shouldBe(visible).click();
        $(".details-self__title").shouldBe(visible);
        $(".details-self__name-text").shouldHave(Condition.text("Санкт-Петербург"));
        String spb_adress = $(".details-self__name-text").getText();
        $(".details-self__btn").click();
        sleep(1000);
        $("span[data-wba-header-name='DLV_Adress']").shouldHave(Condition.text(spb_adress));
        $("a.product-card__add-basket.j-add-to-basket.btn-main").click();
        String productInfo = $("span.product-card__name").text()
                .replaceAll("\\s+","");
        $("span.navbar-pc__notify").shouldHave(Condition.text("1"));
        $("div.navbar-pc__item.j-item-basket").click();
        sleep(1000);
        String amount = $("div.list-item__price-new.wallet").getText();
        $("div.basket-order__b-top.b-top > p > span:nth-child(2) > span")
                .shouldHave(Condition.text(amount));
        String cartInfo = $("span.good-info__good-name").text().replaceAll("\\s+","");
        cartInfo.compareTo(productInfo);
        $("button.b-btn-do-order.btn-main.j-btn-confirm-order").isEnabled();
    }

    @Test
    @DisplayName("Работа с фильтрами")
    void doWithFilter() {
        Selenide.open("https://www.wildberries.ru");
        $("button.nav-element__burger.j-menu-burger-btn.j-wba-header-item").click();
        SelenideElement firstCategory =
                $(".menu-burger__main-list-link.menu-burger__main-list-link--4830");
        firstCategory.shouldBe(visible, enabled).hover();
        SelenideElement secondCategory =
                $("body > div.wrapper > div.menu-burger" +
                        ".j-menu-burger.menu-burger--active > div.menu-burger__drop" +
                        ".j-menu-burger-drop.menu-burger__drop--active.j-menu-active" +
                        ".menu-burger__drop--custom > div > div.menu-burger__drop-list-item.j-menu-drop-item" +
                        ".j-menu-drop-item-4830.menu-burger__drop-list-item--active > div > div.menu-burger__first" +
                        ".j-menu-inner-column > ul > li:nth-child(7) > span");
        secondCategory.shouldBe(visible, enabled).click();
        SelenideElement thirdCategory = $("body > div.wrapper > div.menu-burger.j-menu-burger." +
                "menu-burger--active > div.menu-burger__drop.j-menu-burger-drop.menu-burger__drop--active" +
                ".j-menu-active.menu-burger__drop--custom > div > div.menu-burger__drop-list-item.j-menu-drop-item." +
                "j-menu-drop-item-4830.menu-burger__drop-list-item--active > div > div.menu-burger__second" +
                ".j-menu-inner-column.menu-burger__second--active > ul > li:nth-child(1) > a");
        thirdCategory.shouldBe(visible, enabled).click();
        $("h1").shouldHave(Condition.text("Ноутбуки"));
        $("button.dropdown-filter__btn.dropdown-filter__btn--all").click();
        $(byName("startN")).setValue("100000");
        $(byName("endN")).setValue("149000");
        $(byText("до 3 дней")).click();
        $(byText("Apple")).click();
        $(byText("13.3\"")).click();
        $(byText("Показать")).click();
        $(".goods-count > span").shouldBe(visible);
        int filterCount = Integer.parseInt($(".goods-count > span").text()
                .replaceAll("\\D", ""));
        int searchCount = $$(".product-card-list .product-card").size();
        assert filterCount == searchCount;
        $(byText("от 100 000 до 149 000")).shouldBe(visible);
        $(byText("до 3 дней")).shouldBe(visible);
        $(byText("Apple")).shouldBe(visible);
        $(byText("13.3\"")).shouldBe(visible);
        $(byText("Сбросить все")).shouldBe(visible).isEnabled();
    }
}
