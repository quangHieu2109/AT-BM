function addAddress() {
    var houseNumber = document.getElementById('houseNumber').value;
    var district = document.getElementById('selectDistrict').value;
    var province = document.getElementById('selectProvince').value;
    var ward = document.getElementById('selectWard').value;
   _addAddress(district, province, ward,houseNumber);


}
async function _addAddress(district, province, ward,houseNumber){
    const response =  await fetch(contextPathMetaTag.content + "/addAddress?district="+district+"&province="+province+"&ward="+ward+"&houseNumber="+houseNumber, {
        method: "POST",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
        },
    });
    return [response.status, await response.json()];
}