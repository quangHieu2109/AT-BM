var curPage = 1;
var curLevel = 1;
var totalPage;
$(document).ready(function () {
        loadData(1)
    }
)

function loadData(level) {
    $.ajax({
        url: "/logManagerServlet",
        type: "POST",
        data: {
            page: curPage, level: level
        },
        success: function (response) {
            totalPage = response.totalPage;
            let data = response.data;
            let start = Math.max(1, curPage - 2);
            let end = Math.min(curPage + 2, totalPage)
            let paging = $('#paging'); paging.empty();
            let preDisabled = curPage == 1 ? "disabled" : "";
            let nextDisabled = curPage == totalPage ? "disabled" : "";
            let tableBody = $('#table_body'); tableBody.empty();
            for (let i = 0; i < data.length; i++) {
                let curData = data[i]
                let tr = `<tr class="level${curData.levelLog}">
                <td class="w-10">${curData.id}</td>
                <td class="w-10">${curData.ip}</td>
                <td class="w-10">${curData.levelLog}</td>
                <td class="w-10">${curData.res}</td>
                <td class=" w-20"><p class="limitLine-3">${curData.preValue}</p></td>
                <td class=" w-20"><p class="limitLine-3">${curData.curValue}</p></td>
                <td class="w-10">${curData.createAt}</td>
                <td class="w-10">${curData.updateAt}</td>
                </tr>`;
                tableBody.append(tr);
            }

            paging.append(`<label class="page-link" onclick="changePage(${curPage - 1})" ${preDisabled}>Trang trước</label>`)
            if (curPage - 3 > 1) {
                paging.append(`<input type="radio" name="page" value="1" id="1" hidden
                                   onchange="changePage(1)" >
                                    <label class="page-link"  for="1">1</label>`)
                paging.append(`<label class="page-link"  >...</label>`)
            }
            for (let i = start; i <= end; i++) {
                let page = `<input type="radio" name="page" value="${i}" id="${i}" hidden
                                    onchange="changePage(${i})">
                                    <label class="page-link"  for="${i}">${i}</label>`
                paging.append(page)
            }
            if (curPage + 3 < totalPage) {
                paging.append(`<label class="page-link"  >...</label>`)
                paging.append(`<input type="radio" name="page" value="${totalPage}" id="${totalPage}" hidden
                                   onchange="changePage(${totalPage})">
                                    <label class="page-link"  for="${totalPage}">${totalPage}</label>`)
            }
            paging.append(`<label class="page-link" onclick="changePage(${curPage + 1})" ${nextDisabled} >Trang sau</label>`)
            $('#' + curPage).prop('checked', true)
        },
        error: function (response) {
            console.log(response)
        }
    })
}

function changePage(page) {

    if (page > 0 && page < totalPage + 1) {
        curPage = page;
        $('#' + page).prop('checked', true)
        loadData(curLevel);
    }
}