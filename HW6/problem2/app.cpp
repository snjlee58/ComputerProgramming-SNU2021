#include <iostream>
#include "app.h"
#include "view.h"
#include "frontend.h"
#include "ui.h"
#include "backend.h"

App::App(std::istream& is, std::ostream& os): is(is), os(os) {
    // TODO


}



void App::run() {
    // TODO
    BackEnd backend;
    FrontEnd frontend(backend);
    UserInterface ui(is, os, frontend);
    ui.run();

}
