import { Link } from "react-router-dom";
import styles from '../resources/css/Header.module.css';

export const Header = ({ current }) => {
    return (
        <header>
            <div className={styles.wrapper}>
                <Link to={"/"}>
                    <svg>
                        <text x="50%" y="50%" dy=".35em" textAnchor="middle">
                            Airport API ✈
                        </text>
                    </svg>
                </Link>
            </div>
            <nav>
                <ul>
                    <li className={current === "home" ? styles.selected : ""}>
                        <Link to={"/"}>Home</Link>
                    </li>
                    <li className={current === "dashboard" ? styles.selected : ""}>
                        <Link to={"/dashboard"}>Dashboard</Link>
                    </li>
                    <li className={current === "docs" ? styles.selected : ""}>
                        <Link to={"/docs"}>Documentation</Link>
                    </li>
                    <li>
                        <Link to={"/logout"}>Logout</Link>
                    </li>
                </ul>
            </nav>
        </header>
    )
}

export default Header;
