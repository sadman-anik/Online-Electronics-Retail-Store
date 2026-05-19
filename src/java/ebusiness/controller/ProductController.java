package ebusiness.controller;

import ebusiness.ejb.ProductEJB;
import ebusiness.entity.Product;
import ebusiness.entity.Smartwatch;
import ebusiness.entity.Tablet;
import ebusiness.util.ValidationUtil;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.util.List;

@Named("productController")
@RequestScoped
public class ProductController {

    @EJB
    private ProductEJB productEJB;

    private Tablet tablet = new Tablet();
    private Smartwatch smartwatch = new Smartwatch();
    private String searchModel;
    private List<Tablet> tabletList;
    private List<Smartwatch> smartwatchList;
    private List<Product> productList;

    public String doCreateTablet() {
        if (!validateProduct(tablet)) return null;
        productEJB.createTablet(tablet);
        tablet = new Tablet(); // reset form
        tabletList = null;     // invalidate cache so getter refreshes
        tabletList = productEJB.findTablets();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfully created the tablet: " + tablet.getBrandModel()));
        return "listTablets.xhtml";
    }

    public String doCreateSmartwatch() {
        if (!validateProduct(smartwatch)) return null;
        productEJB.createSmartwatch(smartwatch);
        smartwatchList = productEJB.findSmartwatches();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfully created the smartwatch: " + smartwatch.getBrandModel()));
        return "listSmartwatches.xhtml";
    }

    private boolean validateProduct(Product product) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ValidationUtil.isBlank(product.getBrand())) ctx.addMessage(null, new FacesMessage("Brand is required."));
        if (ValidationUtil.isBlank(product.getModel())) ctx.addMessage(null, new FacesMessage("Model is required."));
        if (!ValidationUtil.isPositive(product.getPrice())) ctx.addMessage(null, new FacesMessage("Price must be greater than zero."));
        if (!ValidationUtil.isPositive(product.getStockNumber())) ctx.addMessage(null, new FacesMessage("Stock number must be greater than zero."));
        return ctx.getMessageList().isEmpty();
    }

    public String doSearchTablet() {
        tabletList = productEJB.searchTablets(ValidationUtil.trimToEmpty(searchModel));
        return "foundTablets.xhtml";
    }

    public String doSearchSmartwatch() {
        smartwatchList = productEJB.searchSmartwatches(ValidationUtil.trimToEmpty(searchModel));
        return "foundSmartwatches.xhtml";
    }

    public Tablet getTablet() { return tablet; }
    public void setTablet(Tablet tablet) { this.tablet = tablet; }
    public Smartwatch getSmartwatch() { return smartwatch; }
    public void setSmartwatch(Smartwatch smartwatch) { this.smartwatch = smartwatch; }
    public String getSearchModel() { return searchModel; }
    public void setSearchModel(String searchModel) { this.searchModel = searchModel; }
    public List<Tablet> getTabletList() {
        if (tabletList == null) {
            tabletList = productEJB.findTablets();
        }
        return tabletList;
    }
    public void setTabletList(List<Tablet> tabletList) { this.tabletList = tabletList; }
    public List<Smartwatch> getSmartwatchList() {
        if (smartwatchList == null) {
            smartwatchList = productEJB.findSmartwatches();
        }
        return smartwatchList;
    }
    public void setSmartwatchList(List<Smartwatch> smartwatchList) { this.smartwatchList = smartwatchList; }
    public List<Product> getProductList() { return productEJB.findProducts(); }
    public void setProductList(List<Product> productList) { this.productList = productList; }
}
