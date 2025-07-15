/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


 function handleProductTypeChange() {
        const productTypeSelect = document.getElementById('categoryId');
        const selectedOption = productTypeSelect.options[productTypeSelect.selectedIndex];
        const selectedType = selectedOption ? selectedOption.dataset.normalizedName : '';
        document.getElementById('productType').value = selectedType;
        const allFieldIds = ['gameFields', 'accessoryFields', 'headphoneFields', 'keyboardFields', 'mouseFields', 'controllerFields'];
        allFieldIds.forEach(id => {
            const element = document.getElementById(id);
            if (element)
                element.style.display = 'none';
        });
        if (!selectedType)
            return;
        const brandSelect = document.querySelector('select[name="brandId"]');
        if (selectedType === 'game') {
            document.getElementById('gameFields').style.display = 'block';
            if (brandSelect) {
                brandSelect.required = false;
            }
        } else {
            const accessoryFields = document.getElementById('accessoryFields');
            if (accessoryFields)
                accessoryFields.style.display = 'block';
            const specificFieldId = selectedType + 'Fields';
            const specificFields = document.getElementById(specificFieldId);
            if (specificFields) {
                specificFields.style.display = 'block';
            }
            if (brandSelect) {
                brandSelect.required = true;
            }
        }
    }

    function setupImageUploader(index) {
        const input = document.getElementById('productImage' + index);
        const uploader = document.getElementById('uploader' + index);
        if (!input || !uploader)
            return;
        const preview = document.getElementById('preview' + index);
        const removeBtn = document.getElementById('removeBtn' + index);
        const uploaderContent = uploader.querySelector('.image-uploader__content');
        input.addEventListener('change', function (event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    preview.src = e.target.result;
                    preview.style.display = 'block';
                    removeBtn.style.display = 'flex';
                    uploaderContent.style.display = 'none';
                }
                reader.readAsDataURL(file);
            }
        });
        removeBtn.addEventListener('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            input.value = '';
            preview.src = '';
            preview.style.display = 'none';
            removeBtn.style.display = 'none';
            uploaderContent.style.display = 'block';
        });
    }

    function toggleDropdown() {
        document.getElementById("adminDropdown").classList.toggle("show");
    }

    function setupToggleButtonGroup(groupId, hiddenInputId) {
        const buttonGroup = document.getElementById(groupId);
        const hiddenInput = document.getElementById(hiddenInputId);
        if (!buttonGroup || !hiddenInput)
            return;
        buttonGroup.addEventListener('click', function (event) {
            if (event.target.classList.contains('toggle-button')) {
                event.target.classList.toggle('active');
                const selectedValues = [];
                buttonGroup.querySelectorAll('.toggle-button.active').forEach(button => {
                    selectedValues.push(button.dataset.value);
                });
                hiddenInput.value = selectedValues.join(',');
            }
        });
    }

    document.addEventListener('DOMContentLoaded', function () {
        for (let i = 0; i < 6; i++) {
            setupImageUploader(i);
        }
        handleProductTypeChange();
        
        setupToggleButtonGroup('platforms-group', 'selectedPlatforms');
        setupToggleButtonGroup('os-group', 'selectedOperatingSystems');
    });