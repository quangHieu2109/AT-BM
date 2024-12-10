$(document).ready(function () {
    loadTable(0)

})

function loadTable(status) {
    if ($.fn.DataTable.isDataTable('#my_table')) {
        $('#my_table').DataTable().destroy()
    }
    $('#my_table__content').css('display', 'block');
    $('#detail_table__content').css('display', 'none')
    $('#my_table').DataTable({

        processing: true,
        serverSide: true,
        ajax: {
            url: "/orderManagerServlet2?type=order&status=" + status,
            dataSrc: "data",
            type: "get"
        },
        columns: [
            {"data": "id"},
            {"data": "idUser"},
            {"data": "deliveryMethod"},
            {"data": "deliveryPrice"},
            {"data": "productsPrice"},
            {"data": "totalPrice"},
            {"data": "createAt"},
            {"data": "updateAt"},
            {"data": "updateStatus"},
            {"data": "operation"}
        ]
    })
    // function

}

function updateStatus(id) {
    let value = $('#sl' + id).val();
    console.log(value)
    $.ajax({
        url: "/orderManagerServlet2",
        type: "post",
        data: {
            "id": id,
            "value": value
        },
        success: function (response) {
            let status = $('input[name="category"]:checked').val();
            // loadTable(status)
            $('#my_table').DataTable().ajax.reload()
            alert("Cập nhật trạng thái đơn hàng " + id + " thành công")
        },
        error: function (response) {
            console.log(response)
            alert(response.responseJSON.error_notifica)
        }
    })
}

function changeStatus(id, value) {
    id2 = "p" + id;


    if ($('#' + id2).css('background-color') != 'rgb(198, 123, 43)') {
        $('#' + id2).on('click', function () {
            updateStatus(id)
        })
        $('#' + id2).css('background-color', '#c67b2b');
    }

}

function detail(id) {
    $('#detail').prop('checked', true);
    $('#my_table__content').css('display', 'none');
    $('#detail_table__content').css('display', 'flex');

    $('#detail_table__content').empty()

        $.ajax( {
            url: "/orderManagerServlet2?type=detail&orderId=" + id,
            type: "get",
            success: function(response){
                let products = response.products;
                $('#detail_table__content').append(`<main class="col-md-9">
            <article class="card mb-4">

              <header class="card-header">
                <strong class="d-inline-block me-4">${response.orderId}</strong>
                <span>${response.createdAt}</span>
                
                    <span class="badge bg-danger float-end">${response.status}</span>
                 
              </header> <!-- card-header.// -->

              <div class="card-body pb-0">
                <div class="row">
                  <div class="col-lg-8">
                    <h6 class="text-muted">${response.receiverInfo}</h6>
                    <p class="lh-lg">
                        ${response.fullname} <br>
                          ${response.phoneNumber} <br>
                    </p>
                  </div>
                  <div class="col-lg-4">
                    <h6 class="text-muted">${response.paymentMethod}</h6>
                    <span class="text-success">
                    <i class="fab fa-lg fa-cc-visa"></i>
                    ${response.deliveryMethod }

                  </span>
                    <p class="lh-lg">
                     ${response.tempPrice}₫ <br>
                     ${response.deliveryPrice}₫
                      <br>
                      <strong>
                       ${response.totalPrice}₫
                      </strong>
                    </p>
                  </div>
                </div> <!-- row.// -->
              </div> <!-- card-body.// -->

              <hr class="m-0">

              <div class="table-responsive">
                <table class="cart-table table table-borderless">
                  <thead class="text-muted">
                  <tr class="small text-uppercase">
                    <th scope="col" style="min-width: 280px;">${response.product}</th>
                    <th scope="col" style="min-width: 150px;">${response.price}</th>
                    <th scope="col" style="min-width: 150px;">${response.quantity}</th>
                  </tr>
                  </thead>
                  <tbody id="cart_items">
                  
                  </tbody>
                </table>
              </div> <!-- table.responsive-md.// -->
            </article>
          </main>`)
            let cart_items = $('#cart_items');
            for(let i=0; i<products.length; i++){
                let product = products[i];
                cart_items.append(`
                    <tr>
                      <td>
                        <figure class="itemside">
                          <div class="float-start me-3">
                                ${product.image}
                          </div>
                          <figcaption class="info">
                                ${product.info}
                          </figcaption>
                        </figure>
                      </td>
                      <td>
                        <div class="price-wrap">
                             
                                ${product.price}₫
                              
                        </div>
                      </td>
                      <td>${product.quantity}</td>
                    </tr>
                  `)

            }
            },
            error: function(response){
                console.log(response);
            }
        }


    )

}