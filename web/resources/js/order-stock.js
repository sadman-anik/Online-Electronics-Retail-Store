document.addEventListener("DOMContentLoaded", function () {
    var product = document.querySelector("select[id$='product']");
    var quantity = document.querySelector("input[id$='quantity']");

    if (!product || !quantity) {
        return;
    }

    var warning = document.createElement("div");
    warning.className = "message stock-warning";
    warning.setAttribute("aria-live", "polite");
    warning.style.display = "none";
    quantity.parentNode.appendChild(warning);

    var totalPreview = document.createElement("div");
    totalPreview.className = "order-total-preview";
    totalPreview.setAttribute("aria-live", "polite");
    quantity.parentNode.appendChild(totalPreview);

    function selectedOption() {
        var option = product.options[product.selectedIndex];
        return option || null;
    }

    function selectedStock() {
        var option = selectedOption();
        if (!option) {
            return null;
        }

        var match = option.text.match(/\(Stock:\s*(\d+)\)/);
        return match ? parseInt(match[1], 10) : null;
    }

    function selectedPrice() {
        var option = selectedOption();
        if (!option) {
            return null;
        }

        var match = option.text.match(/Price:\s*\$?([0-9]+(?:\.[0-9]+)?)/);
        return match ? parseFloat(match[1]) : null;
    }

    function formatMoney(value) {
        return "$" + value.toFixed(2);
    }

    function updateTotalPreview() {
        var price = selectedPrice();
        var requested = parseInt(quantity.value, 10);

        if (price === null || !requested || requested <= 0) {
            totalPreview.textContent = "";
            return;
        }

        totalPreview.textContent = "Unit price: " + formatMoney(price)
                + " | Estimated total: " + formatMoney(price * requested);
    }

    function validateStock() {
        var stock = selectedStock();
        var requested = parseInt(quantity.value, 10);

        if (stock === null || !requested || requested <= stock) {
            warning.style.display = "none";
            warning.textContent = "";
            return true;
        }

        warning.textContent = "Only " + stock + " units remain for the selected product.";
        warning.style.display = "block";
        return false;
    }

    function updateOrderFeedback() {
        updateTotalPreview();
        return validateStock();
    }

    product.addEventListener("change", updateOrderFeedback);
    quantity.addEventListener("input", updateOrderFeedback);

    quantity.form.addEventListener("submit", function (event) {
        if (!updateOrderFeedback()) {
            event.preventDefault();
        }
    });

    updateOrderFeedback();
});
