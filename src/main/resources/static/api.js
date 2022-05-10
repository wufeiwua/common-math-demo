function linear(){
  return fetch("/linear").then(res=>res.text()).then(res=>JSON.parse(res))
}

function simple(){
  return fetch("/simple").then(res=>res.text()).then(res=>JSON.parse(res))
}

function customize(){
  return fetch("/customize").then(res=>res.text()).then(res=>JSON.parse(res))
}

function multiple(){
  return fetch("/multiple").then(res=>res.text()).then(res=>JSON.parse(res))
}