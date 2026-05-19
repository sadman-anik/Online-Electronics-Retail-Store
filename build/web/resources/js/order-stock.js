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

    function selectedStock() {
        var option = product.options[product.selectedIndex];
        if (!option) {
            return null;
        }

        var match = option.text.match(/\(Stock:\s*(\d+)\)/);
        return match ? parseInt(match[1], 10) : null;
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

    product.addEventListener("change", validateStock);
    quantity.addEventListener("input", validateStock);

    quantity.form.addEventListener("submit", function (event) {
        if (!validateStock()) {
            event.preventDefault();
        }
    });
});
