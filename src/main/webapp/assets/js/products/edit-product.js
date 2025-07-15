/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
function handleProductTypeChange() {
        const select = document.getElementById('categoryId');
        const selectedOption = select.options[select.selectedIndex];
        const type = selectedOption ? selectedOption.dataset.normalizedName : '';

        document.getElementById('productType').value = type;

        const allDynamicFieldIds = [
            'gameFields', 'accessoryFields', 'headphoneFields', 
            'keyboardFields', 'mouseFields', 'controllerFields'
        ];
        
        allDynamicFieldIds.forEach(id => {
            const element = document.getElementById(id);
            if (element) element.style.display = 'none';
        });

        const brandContainer = document.getElementById('brandFieldContainer');

        if (type === 'game') {
            document.getElementById('gameFields').style.display = 'block';
            brandContainer.style.display = 'none';
        } else if (type) {
            document.getElementById('accessoryFields').style.display = 'block';
            brandContainer.style.display = 'block';

            const specificFields = document.getElementById(type + 'Fields');
            if (specificFields) {
                specificFields.style.display = 'block';
            }
        } else { 
            brandContainer.style.display = 'block';
        }
    }

    function setupImageUploader(index) {
        const input = document.getElementById('productImage' + index);
        const uploader = document.getElementById('uploader' + index);
        if (!input || !uploader) return;

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
                };
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
            uploaderContent.style.display = 'flex';
        });
    }

    document.addEventListener('DOMContentLoaded', function () {
        handleProductTypeChange();

        for (let i = 0; i < 6; i++) {
            setupImageUploader(i);
        }
    });

