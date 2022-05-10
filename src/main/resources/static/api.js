function linear(){
  return fetch("/commons-math/api/linear").then(res=>res.text()).then(res=>JSON.parse(res))
}

function simple(){
  return fetch("/commons-math/api/simple").then(res=>res.text()).then(res=>JSON.parse(res))
}

function customize(){
  return fetch("/commons-math/api/customize").then(res=>res.text()).then(res=>JSON.parse(res))
}

function multiple(){
  return fetch("/commons-math/api/multiple").then(res=>res.text()).then(res=>JSON.parse(res))
}