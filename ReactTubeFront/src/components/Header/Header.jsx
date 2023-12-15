/* eslint-disable react/prop-types */
import { Link } from "react-router-dom";

import styles from './Header.module.css';

import close from "../../resources/img/close.png";
import menu from "../../resources/img/menu.png";

import Search from "../Search/Search.jsx";
import {useState} from "react";

export const Header = ({ current, setSearchTerm }) => {

    const [showMenu, setShowMenu] = useState(false);

    return (
        <header>
            <div className={styles.wrapper}>
                <Link to={"/home"}>
                    <svg>
                        <text x="50%" y="50%" dy=".35em" textAnchor="middle">
                            {`React-Tube`}
                        </text>     
                    </svg>
                </Link>
            </div>

            <Search setSearchTerm={setSearchTerm}/>

            <nav>
                <img className={styles.showBtn} src={menu} onClick={() => setShowMenu(true)}/>
                <ul className={showMenu && styles.active}>
                    <li className={current === "home" ? styles.selected : ""}>
                        <Link to={"/home"}>Home <span>⌂</span></Link>
                    </li>
                    <li className={current === "dashboard" ? styles.selected : ""}>
                        <Link to={"/dashboard"}>Dashboard <span>ℹ</span> </Link>
                    </li>
                    <li>
                        <Link to={"/uploadVideo"}>
                            Upload Video <span>⇪</span>
                        </Link>
                    </li>
                    <li>
                        <Link to={"/logout"}>
                            Logout <span>↪</span>
                        </Link>
                    </li>
                    <img src={close} alt="close" className={styles.closeBtn} onClick={() => setShowMenu(false)}/>
                </ul>
            </nav>
        </header>
    )
}

export default Header;
