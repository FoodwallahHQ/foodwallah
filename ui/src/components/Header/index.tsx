import React, {FC} from 'react';
import {observer} from "mobx-react";
import {PATHS} from "@/utils/constants";
import Link from "next/link";

export interface HeaderNavProps {
}

const HeaderNav: FC<HeaderNavProps> = observer((_props) => {
  return (
      <header className="header">
        <nav className="navbar navbar-expand-lg menu_three sticky-nav">
          <div className="container-fluid">
            <Link className="navbar-brand header_logo" href={PATHS.HOME}>
              <img className="main_logo" src="/assets/images/kalico.png" alt="logo"/>
            </Link>
            <button
                className="navbar-toggler collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#navbarText"
                aria-controls="navbarText"
                aria-expanded="false"
                aria-label="Toggle navigation"
            >
            <span className="menu_toggle">
              <span className="hamburger">
                <span></span>
                <span></span>
                <span></span>
              </span>
              <span className="hamburger-cross">
                <span></span>
                <span></span>
              </span>
            </span>
            </button>
            <div className="collapse navbar-collapse justify-content-end" id="navbarText">
              <div className="right-nav">
                <a href="/"><p>Home</p></a>
                <a href="/recipes/1"><p>Recipes</p></a>
                {/*<a href="/about"><p>About</p></a>*/}
              </div>
            </div>
          </div>
        </nav>
      </header>
  );
})
export default HeaderNav;
