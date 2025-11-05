--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

-- Started on 2025-06-03 01:11:40

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 17455)
-- Name: assigned_employees; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.assigned_employees (
    branch_id integer NOT NULL,
    week_start date NOT NULL,
    shift_id integer NOT NULL,
    username character varying(100) NOT NULL,
    role character varying(50) NOT NULL,
    start_time time without time zone,
    end_time time without time zone
);


ALTER TABLE public.assigned_employees OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 17458)
-- Name: branches; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.branches (
    branch_id integer NOT NULL,
    city character varying(100),
    shipping_area character varying(200),
    phone character varying(20),
    contact_name character varying(100)
);


ALTER TABLE public.branches OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 17461)
-- Name: deliveries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.deliveries (
    id integer,
    delivery_day integer,
    delivery_date date,
    departure_time text,
    arrival_time text,
    truck_license text,
    driver_id text,
    source_address text,
    branch_id integer,
    status text,
    total_weight double precision
);


ALTER TABLE public.deliveries OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 17466)
-- Name: deliveries_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.deliveries_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.deliveries_id_seq OWNER TO postgres;

--
-- TOC entry 4889 (class 0 OID 0)
-- Dependencies: 220
-- Name: deliveries_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.deliveries_id_seq OWNED BY public.deliveries.id;


--
-- TOC entry 221 (class 1259 OID 17467)
-- Name: delivery_destinations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.delivery_destinations (
    delivery_id integer NOT NULL,
    site_address character varying(200) NOT NULL,
    item_name character varying(100) NOT NULL,
    item_quantity integer NOT NULL,
    item_weight real NOT NULL
);


ALTER TABLE public.delivery_destinations OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 17470)
-- Name: delivery_reports; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.delivery_reports (
    delivery_id integer,
    source_address text,
    source_id integer,
    time_stamp text,
    driver_id text,
    truck_license text,
    actual_departure_time text,
    total_weight double precision,
    status text,
    notes text
);


ALTER TABLE public.delivery_reports OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 17475)
-- Name: destination_reports; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.destination_reports (
    delivery_id integer,
    destination_address text
);


ALTER TABLE public.destination_reports OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 17480)
-- Name: employee_availability; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employee_availability (
    username character varying(100) NOT NULL,
    branch_id integer NOT NULL,
    week_start date NOT NULL,
    shift_id integer NOT NULL,
    role character varying(50) NOT NULL
);


ALTER TABLE public.employee_availability OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 17483)
-- Name: employee_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employee_roles (
    username character varying(100) NOT NULL,
    role character varying(50) NOT NULL
);


ALTER TABLE public.employee_roles OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 17486)
-- Name: employees; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employees (
    username character varying(100) NOT NULL,
    password character varying(100),
    branch_id integer,
    is_logged_in boolean,
    bank_account character varying(50),
    is_shift_manager boolean,
    is_hr_manager boolean,
    hourly_salary integer,
    start_contract date,
    end_contract date,
    fired_date date
);


ALTER TABLE public.employees OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 17489)
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    role_name character varying(50) NOT NULL
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 17492)
-- Name: shift_requirements; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.shift_requirements (
    branch_id integer NOT NULL,
    week_start date NOT NULL,
    shift_id integer NOT NULL,
    role character varying(50) NOT NULL,
    required_amount integer
);


ALTER TABLE public.shift_requirements OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 17495)
-- Name: shifts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.shifts (
    shift_id integer NOT NULL,
    week_start date NOT NULL,
    date date,
    start_time time without time zone,
    end_time time without time zone,
    branch_id integer NOT NULL,
    is_vacation boolean,
    shift_type character varying(20),
    is_published boolean DEFAULT false
);


ALTER TABLE public.shifts OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 17499)
-- Name: truck_schedule; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.truck_schedule (
    license_plate text,
    shift_number integer,
    status text,
    assigned_delivery_id text
);


ALTER TABLE public.truck_schedule OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 17504)
-- Name: trucks; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trucks (
    license_plate text NOT NULL,
    required_license text,
    truck_weight double precision,
    max_weight double precision,
    model text
);


ALTER TABLE public.trucks OWNER TO postgres;

--
-- TOC entry 4693 (class 2604 OID 17509)
-- Name: deliveries id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deliveries ALTER COLUMN id SET DEFAULT nextval('public.deliveries_id_seq'::regclass);


--
-- TOC entry 4869 (class 0 OID 17455)
-- Dependencies: 217
-- Data for Name: assigned_employees; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 0, 'e_1_b0_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 0, 'e_1_b0_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 0, 'e_1_b0_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 0, 'e_1_b0_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 0, 'e_1_b0_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 0, 'DriverManager_0', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 0, 'Hr_0', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 0, 'e_1_b0_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 0, 'e_1_b0_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 1, 'e_1_b0_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 1, 'e_1_b0_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 1, 'e_1_b0_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 1, 'e_1_b0_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 1, 'e_1_b0_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 1, 'DriverManager_0', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 1, 'Hr_0', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 1, 'e_1_b0_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 1, 'e_1_b0_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 2, 'e_1_b0_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 2, 'e_1_b0_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 2, 'e_1_b0_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 2, 'e_1_b0_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 2, 'e_1_b0_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 2, 'DriverManager_0', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 2, 'Hr_0', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 2, 'e_1_b0_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 2, 'e_1_b0_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 3, 'e_1_b0_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 3, 'e_1_b0_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 3, 'e_1_b0_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 3, 'e_1_b0_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 3, 'e_1_b0_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 3, 'DriverManager_0', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 3, 'Hr_0', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 3, 'e_1_b0_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 3, 'e_1_b0_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 4, 'e_1_b0_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 4, 'e_1_b0_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 4, 'e_1_b0_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 4, 'e_1_b0_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 4, 'e_1_b0_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 4, 'DriverManager_0', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 4, 'Hr_0', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 4, 'e_1_b0_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 4, 'e_1_b0_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 5, 'e_1_b0_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 5, 'e_1_b0_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 5, 'e_1_b0_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 5, 'e_1_b0_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 5, 'e_1_b0_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 5, 'DriverManager_0', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 5, 'Hr_0', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 5, 'e_1_b0_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 5, 'e_1_b0_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 6, 'e_1_b0_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 6, 'e_1_b0_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 6, 'e_1_b0_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 6, 'e_1_b0_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 6, 'e_1_b0_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 6, 'DriverManager_0', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 6, 'Hr_0', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 6, 'e_1_b0_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 6, 'e_1_b0_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 7, 'e_1_b0_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 7, 'e_1_b0_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 7, 'e_1_b0_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 7, 'e_1_b0_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 7, 'e_1_b0_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 7, 'DriverManager_0', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 7, 'Hr_0', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 7, 'e_1_b0_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 7, 'e_1_b0_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 8, 'e_1_b0_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 8, 'e_1_b0_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 8, 'e_1_b0_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 8, 'e_1_b0_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 8, 'e_1_b0_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 8, 'DriverManager_0', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 8, 'Hr_0', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 8, 'e_1_b0_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 8, 'e_1_b0_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 9, 'e_1_b0_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 9, 'e_1_b0_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 9, 'e_1_b0_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 9, 'e_1_b0_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 9, 'e_1_b0_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 9, 'DriverManager_0', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 9, 'Hr_0', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 9, 'e_1_b0_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 9, 'e_1_b0_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 10, 'e_1_b0_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 10, 'e_1_b0_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 10, 'e_1_b0_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 10, 'e_1_b0_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 10, 'e_1_b0_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 10, 'DriverManager_0', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 10, 'Hr_0', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 10, 'e_1_b0_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 10, 'e_1_b0_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 11, 'e_1_b0_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 11, 'e_1_b0_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 11, 'e_1_b0_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 11, 'e_1_b0_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 11, 'e_1_b0_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 11, 'DriverManager_0', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 11, 'Hr_0', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 11, 'e_1_b0_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 11, 'e_1_b0_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 12, 'e_1_b0_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 12, 'e_1_b0_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 12, 'e_1_b0_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 12, 'e_1_b0_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 12, 'e_1_b0_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 12, 'DriverManager_0', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 12, 'Hr_0', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 12, 'e_1_b0_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 12, 'e_1_b0_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 13, 'e_1_b0_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 13, 'e_1_b0_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 13, 'e_1_b0_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 13, 'e_1_b0_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 13, 'e_1_b0_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 13, 'DriverManager_0', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 13, 'Hr_0', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 13, 'e_1_b0_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-05-25', 13, 'e_1_b0_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 0, 'e_1_b1_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 0, 'e_1_b1_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 0, 'e_1_b1_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 0, 'e_1_b1_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 0, 'e_1_b1_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 0, 'DriverManager_1', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 0, 'Hr_1', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 0, 'e_1_b1_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 0, 'e_1_b1_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 1, 'e_1_b1_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 1, 'e_1_b1_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 1, 'e_1_b1_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 1, 'e_1_b1_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 1, 'e_1_b1_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 1, 'DriverManager_1', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 1, 'Hr_1', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 1, 'e_1_b1_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 1, 'e_1_b1_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 2, 'e_1_b1_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 2, 'e_1_b1_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 2, 'e_1_b1_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 2, 'e_1_b1_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 2, 'e_1_b1_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 2, 'DriverManager_1', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 2, 'Hr_1', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 2, 'e_1_b1_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 2, 'e_1_b1_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 3, 'e_1_b1_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 3, 'e_1_b1_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 3, 'e_1_b1_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 3, 'e_1_b1_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 3, 'e_1_b1_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 3, 'DriverManager_1', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 3, 'Hr_1', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 3, 'e_1_b1_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 3, 'e_1_b1_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 4, 'e_1_b1_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 4, 'e_1_b1_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 4, 'e_1_b1_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 4, 'e_1_b1_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 4, 'e_1_b1_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 4, 'DriverManager_1', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 4, 'Hr_1', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 4, 'e_1_b1_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 4, 'e_1_b1_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 5, 'e_1_b1_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 5, 'e_1_b1_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 5, 'e_1_b1_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 5, 'e_1_b1_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 5, 'e_1_b1_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 5, 'DriverManager_1', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 5, 'Hr_1', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 5, 'e_1_b1_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 5, 'e_1_b1_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 6, 'e_1_b1_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 6, 'e_1_b1_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 6, 'e_1_b1_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 6, 'e_1_b1_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 6, 'e_1_b1_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 6, 'DriverManager_1', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 6, 'Hr_1', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 6, 'e_1_b1_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 6, 'e_1_b1_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 7, 'e_1_b1_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 7, 'e_1_b1_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 7, 'e_1_b1_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 7, 'e_1_b1_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 7, 'e_1_b1_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 7, 'DriverManager_1', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 7, 'Hr_1', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 7, 'e_1_b1_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 7, 'e_1_b1_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 8, 'e_1_b1_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 8, 'e_1_b1_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 8, 'e_1_b1_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 8, 'e_1_b1_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 8, 'e_1_b1_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 8, 'DriverManager_1', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 8, 'Hr_1', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 8, 'e_1_b1_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 8, 'e_1_b1_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 9, 'e_1_b1_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 9, 'e_1_b1_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 9, 'e_1_b1_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 9, 'e_1_b1_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 9, 'e_1_b1_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 9, 'DriverManager_1', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 9, 'Hr_1', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 9, 'e_1_b1_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 9, 'e_1_b1_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 10, 'e_1_b1_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 10, 'e_1_b1_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 10, 'e_1_b1_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 10, 'e_1_b1_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 10, 'e_1_b1_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 10, 'DriverManager_1', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 10, 'Hr_1', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 10, 'e_1_b1_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 10, 'e_1_b1_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 11, 'e_1_b1_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 11, 'e_1_b1_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 11, 'e_1_b1_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 11, 'e_1_b1_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 11, 'e_1_b1_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 11, 'DriverManager_1', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 11, 'Hr_1', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 11, 'e_1_b1_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 11, 'e_1_b1_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 12, 'e_1_b1_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 12, 'e_1_b1_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 12, 'e_1_b1_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 12, 'e_1_b1_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 12, 'e_1_b1_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 12, 'DriverManager_1', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 12, 'Hr_1', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 12, 'e_1_b1_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 12, 'e_1_b1_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 13, 'e_1_b1_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 13, 'e_1_b1_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 13, 'e_1_b1_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 13, 'e_1_b1_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 13, 'e_1_b1_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 13, 'DriverManager_1', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 13, 'Hr_1', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 13, 'e_1_b1_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-05-25', 13, 'e_1_b1_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 0, 'DriverManager_0', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 0, 'e_1_b0_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 0, 'e_1_b0_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 0, 'e_3_b0_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 0, 'TEST', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 0, 'TEST', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 1, 'e_1_b0_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 1, 'e_1_b0_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 1, 'Hr_0', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 2, 'e_2_b0_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 2, 'Hr_0', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 2, 'TEST', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 3, 'e_1_b0_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 3, 'Hr_0', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 4, 'e_1_b0_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 4, 'e_1_b0_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 4, 'TEST', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 5, 'e_1_b0_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 5, 'e_2_b0_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 5, 'e_2_b0_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 5, 'TEST', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 8, 'e_1_b0_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 8, 'e_1_b0_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 8, 'e_3_b0_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 13, 'e_1_b0_ShiftManager', 'ShiftManager', '17:00:00', '22:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 13, 'e_1_b0_StoreKeeper', 'StoreKeeper', '17:00:00', '22:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-01', 13, 'e_3_b0_Cashier', 'Cashier', '17:00:00', '22:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 1, 'DriverManager_1', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 1, 'e_2_b1_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 1, 'e_2_b1_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 1, 'e_2_b1_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 1, 'e_2_b1_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 1, 'e_2_b1_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 1, 'e_2_b1_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 1, 'e_2_b1_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 1, 'Hr_1', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 2, 'DriverManager_1', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 2, 'e_1_b1_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 2, 'e_1_b1_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 2, 'e_1_b1_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 2, 'e_1_b1_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 2, 'e_1_b1_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 2, 'e_1_b1_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 2, 'e_1_b1_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 2, 'Hr_1', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 3, 'DriverManager_1', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 3, 'e_2_b1_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 3, 'e_2_b1_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 3, 'e_2_b1_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 3, 'e_2_b1_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 3, 'e_2_b1_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 3, 'e_2_b1_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 3, 'e_2_b1_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 3, 'Hr_1', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 4, 'DriverManager_1', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 4, 'e_1_b1_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 4, 'e_1_b1_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 4, 'e_1_b1_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 4, 'e_1_b1_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 4, 'e_1_b1_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 4, 'e_1_b1_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 4, 'e_1_b1_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 4, 'Hr_1', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 5, 'DriverManager_1', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 5, 'e_2_b1_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 5, 'e_2_b1_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 5, 'e_2_b1_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 5, 'e_2_b1_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 5, 'e_2_b1_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 5, 'e_2_b1_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 5, 'e_2_b1_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 5, 'Hr_1', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 6, 'DriverManager_1', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 6, 'e_1_b1_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 6, 'e_1_b1_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 6, 'e_1_b1_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 6, 'e_1_b1_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 6, 'e_1_b1_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 6, 'e_1_b1_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 6, 'e_1_b1_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 6, 'Hr_1', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 7, 'DriverManager_1', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 7, 'e_2_b1_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 7, 'e_2_b1_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 7, 'e_2_b1_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 7, 'e_2_b1_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 7, 'e_2_b1_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 7, 'e_2_b1_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 7, 'e_2_b1_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 7, 'Hr_1', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 8, 'DriverManager_1', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 8, 'e_1_b1_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 8, 'e_1_b1_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 8, 'e_1_b1_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 8, 'e_1_b1_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 8, 'e_1_b1_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 8, 'e_1_b1_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 8, 'e_1_b1_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 8, 'Hr_1', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 9, 'DriverManager_1', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 9, 'e_2_b1_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 9, 'e_2_b1_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 9, 'e_2_b1_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 9, 'e_2_b1_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 9, 'e_2_b1_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 9, 'e_2_b1_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 9, 'e_2_b1_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 9, 'Hr_1', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 10, 'DriverManager_1', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 10, 'e_1_b1_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 10, 'e_1_b1_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 10, 'e_1_b1_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 10, 'e_1_b1_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 10, 'e_1_b1_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 10, 'e_1_b1_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 10, 'e_1_b1_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 10, 'Hr_1', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 11, 'DriverManager_1', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 11, 'e_2_b1_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 11, 'e_2_b1_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 11, 'e_2_b1_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 11, 'e_2_b1_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 11, 'e_2_b1_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 11, 'e_2_b1_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 11, 'e_2_b1_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 11, 'Hr_1', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 12, 'DriverManager_1', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 12, 'e_1_b1_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 12, 'e_1_b1_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 12, 'e_1_b1_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 12, 'e_1_b1_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 12, 'e_1_b1_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 12, 'e_1_b1_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 12, 'e_1_b1_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 12, 'Hr_1', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 13, 'DriverManager_1', 'DriverManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 13, 'e_2_b1_Cashier', 'Cashier', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 13, 'e_2_b1_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 13, 'e_2_b1_Driver_B', 'Driver_B', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 13, 'e_2_b1_Driver_C', 'Driver_C', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 13, 'e_2_b1_Driver_D', 'Driver_D', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 13, 'e_2_b1_ShiftManager', 'ShiftManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 13, 'e_2_b1_StoreKeeper', 'StoreKeeper', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 13, 'Hr_1', 'HrManager', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 0, 'DriverManager_1', 'DriverManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 0, 'e_1_b1_Cashier', 'Cashier', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 0, 'e_1_b1_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 0, 'e_1_b1_Driver_B', 'Driver_B', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 0, 'e_1_b1_Driver_C', 'Driver_C', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 0, 'e_1_b1_Driver_D', 'Driver_D', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 0, 'e_1_b1_ShiftManager', 'ShiftManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 0, 'e_1_b1_StoreKeeper', 'StoreKeeper', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (1, '2025-06-01', 0, 'Hr_1', 'HrManager', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-08', 0, 'e_1_b1_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-08', 8, 'e_1_b1_Driver_A', 'Driver_A', '08:00:00', '15:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-08', 9, 'e_2_b0_Driver_A', 'Driver_A', '15:00:00', '20:00:00');
INSERT INTO public.assigned_employees VALUES (0, '2025-06-08', 6, 'e_1_b0_Driver_A', 'Driver_A', '08:00:00', '15:00:00');


--
-- TOC entry 4870 (class 0 OID 17458)
-- Dependencies: 218
-- Data for Name: branches; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.branches VALUES (0, 'Be''er Seva', '1', '055555555', 'Hadar Shir');
INSERT INTO public.branches VALUES (1, 'Tel Aviv', '2', '054444444', 'Aviel Itzhak');


--
-- TOC entry 4871 (class 0 OID 17461)
-- Dependencies: 219
-- Data for Name: deliveries; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.deliveries VALUES (65, 1, NULL, '10:00:00+02', '11:00:00+02', 'TRUCK1', NULL, 'Be''er Seva', 0, 'locked', 3500);
INSERT INTO public.deliveries VALUES (26, 4, NULL, '20:38:40+02', '11:00:00+02', 'TRUCK1', 'e_1_b0_Driver_A', 'Be''er Seva', 0, 'completed', 3501);


--
-- TOC entry 4873 (class 0 OID 17467)
-- Dependencies: 221
-- Data for Name: delivery_destinations; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 4874 (class 0 OID 17470)
-- Dependencies: 222
-- Data for Name: delivery_reports; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.delivery_reports VALUES (1000, 'Be''er Seva', 0, '2025-06-02T16:51:06.575553400', 'e_1_b1_Driver_A', 'TRUCK1', NULL, 3501, 'completed', NULL);
INSERT INTO public.delivery_reports VALUES (1000, 'Be''er Seva', 0, '2025-06-02T16:51:06.575553400', 'e_1_b1_Driver_A', 'TRUCK1', NULL, 3501, 'completed', NULL);
INSERT INTO public.delivery_reports VALUES (65, 'Be''er Seva', 0, '2025-06-02T23:42:33.204242300', NULL, 'TRUCK1', NULL, 3500, 'locked', NULL);
INSERT INTO public.delivery_reports VALUES (26, 'Be''er Seva', 0, '2025-06-02T20:33:07.422642200', 'e_1_b0_Driver_A', 'TRUCK1', NULL, 3501, 'completed', NULL);


--
-- TOC entry 4875 (class 0 OID 17475)
-- Dependencies: 223
-- Data for Name: destination_reports; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.destination_reports VALUES (65, 'Tel Aviv');
INSERT INTO public.destination_reports VALUES (26, 'Tel Aviv');
INSERT INTO public.destination_reports VALUES (26, 'Tel Aviv');


--
-- TOC entry 4876 (class 0 OID 17480)
-- Dependencies: 224
-- Data for Name: employee_availability; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.employee_availability VALUES ('Hr_0', 0, '2025-06-01', 1, 'HrManager');
INSERT INTO public.employee_availability VALUES ('Hr_0', 0, '2025-06-01', 2, 'HrManager');
INSERT INTO public.employee_availability VALUES ('Hr_0', 0, '2025-06-01', 3, 'HrManager');
INSERT INTO public.employee_availability VALUES ('Hr_0', 0, '2025-06-01', 4, 'HrManager');
INSERT INTO public.employee_availability VALUES ('Hr_0', 0, '2025-06-01', 5, 'HrManager');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 0, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 0, 'Cashier');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 0, 'StoreKeeper');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 0, 'Driver_A');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 0, 'Driver_B');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 0, 'Driver_C');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 0, 'Driver_D');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 0, 'DriverManager');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 1, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 1, 'Cashier');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 1, 'StoreKeeper');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 1, 'Driver_A');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 1, 'Driver_B');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 1, 'Driver_C');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 1, 'Driver_D');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 1, 'DriverManager');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 2, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 2, 'Cashier');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 2, 'StoreKeeper');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 2, 'Driver_A');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 2, 'Driver_B');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 2, 'Driver_C');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 2, 'Driver_D');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 2, 'DriverManager');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 3, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 3, 'Cashier');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 3, 'StoreKeeper');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 3, 'Driver_A');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 3, 'Driver_B');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 3, 'Driver_C');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 3, 'Driver_D');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 3, 'DriverManager');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 4, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 4, 'Cashier');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 4, 'StoreKeeper');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 4, 'Driver_A');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 4, 'Driver_B');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 4, 'Driver_C');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 4, 'Driver_D');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 4, 'DriverManager');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 5, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 5, 'Cashier');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 5, 'StoreKeeper');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 5, 'Driver_A');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 5, 'Driver_B');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 5, 'Driver_C');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 5, 'Driver_D');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 5, 'DriverManager');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 6, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 6, 'Cashier');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 6, 'StoreKeeper');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 6, 'Driver_A');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 6, 'Driver_B');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 6, 'Driver_C');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 6, 'Driver_D');
INSERT INTO public.employee_availability VALUES ('TEST', 0, '2025-06-01', 6, 'DriverManager');
INSERT INTO public.employee_availability VALUES ('e_1_b0_ShiftManager', 0, '2025-06-01', 0, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('e_1_b0_ShiftManager', 0, '2025-06-01', 1, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('e_1_b0_ShiftManager', 0, '2025-06-01', 2, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('e_1_b0_ShiftManager', 0, '2025-06-01', 3, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('e_1_b0_ShiftManager', 0, '2025-06-01', 4, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('e_1_b0_ShiftManager', 0, '2025-06-01', 5, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('e_1_b0_ShiftManager', 0, '2025-06-01', 6, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('e_1_b0_ShiftManager', 0, '2025-06-01', 7, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('e_1_b0_ShiftManager', 0, '2025-06-01', 8, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('e_1_b0_ShiftManager', 0, '2025-06-01', 13, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('e_2_b0_ShiftManager', 0, '2025-06-01', 0, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('e_2_b0_ShiftManager', 0, '2025-06-01', 1, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('e_2_b0_ShiftManager', 0, '2025-06-01', 5, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('e_2_b0_ShiftManager', 0, '2025-06-01', 6, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('e_2_b0_ShiftManager', 0, '2025-06-01', 8, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('e_2_b0_ShiftManager', 0, '2025-06-01', 7, 'ShiftManager');
INSERT INTO public.employee_availability VALUES ('e_1_b0_Cashier', 0, '2025-06-01', 0, 'Cashier');
INSERT INTO public.employee_availability VALUES ('e_1_b0_Cashier', 0, '2025-06-01', 1, 'Cashier');
INSERT INTO public.employee_availability VALUES ('e_1_b0_Cashier', 0, '2025-06-01', 2, 'Cashier');
INSERT INTO public.employee_availability VALUES ('e_1_b0_Cashier', 0, '2025-06-01', 3, 'Cashier');
INSERT INTO public.employee_availability VALUES ('e_1_b0_Cashier', 0, '2025-06-01', 4, 'Cashier');
INSERT INTO public.employee_availability VALUES ('e_1_b0_Cashier', 0, '2025-06-01', 5, 'Cashier');
INSERT INTO public.employee_availability VALUES ('e_1_b0_Cashier', 0, '2025-06-01', 6, 'Cashier');
INSERT INTO public.employee_availability VALUES ('e_1_b0_Cashier', 0, '2025-06-01', 7, 'Cashier');
INSERT INTO public.employee_availability VALUES ('e_1_b0_StoreKeeper', 0, '2025-06-01', 8, 'StoreKeeper');
INSERT INTO public.employee_availability VALUES ('e_1_b0_StoreKeeper', 0, '2025-06-01', 4, 'StoreKeeper');
INSERT INTO public.employee_availability VALUES ('e_1_b0_StoreKeeper', 0, '2025-06-01', 5, 'StoreKeeper');
INSERT INTO public.employee_availability VALUES ('e_1_b0_StoreKeeper', 0, '2025-06-01', 6, 'StoreKeeper');
INSERT INTO public.employee_availability VALUES ('e_1_b0_StoreKeeper', 0, '2025-06-01', 7, 'StoreKeeper');


--
-- TOC entry 4877 (class 0 OID 17483)
-- Dependencies: 225
-- Data for Name: employee_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.employee_roles VALUES ('e_1_b0_ShiftManager', 'ShiftManager');
INSERT INTO public.employee_roles VALUES ('e_2_b0_ShiftManager', 'ShiftManager');
INSERT INTO public.employee_roles VALUES ('e_3_b0_ShiftManager', 'ShiftManager');
INSERT INTO public.employee_roles VALUES ('e_1_b0_Cashier', 'Cashier');
INSERT INTO public.employee_roles VALUES ('e_2_b0_Cashier', 'Cashier');
INSERT INTO public.employee_roles VALUES ('e_3_b0_Cashier', 'Cashier');
INSERT INTO public.employee_roles VALUES ('e_1_b0_StoreKeeper', 'StoreKeeper');
INSERT INTO public.employee_roles VALUES ('e_2_b0_StoreKeeper', 'StoreKeeper');
INSERT INTO public.employee_roles VALUES ('e_3_b0_StoreKeeper', 'StoreKeeper');
INSERT INTO public.employee_roles VALUES ('e_1_b0_Driver_A', 'Driver_A');
INSERT INTO public.employee_roles VALUES ('e_2_b0_Driver_A', 'Driver_A');
INSERT INTO public.employee_roles VALUES ('e_3_b0_Driver_A', 'Driver_A');
INSERT INTO public.employee_roles VALUES ('e_1_b0_Driver_B', 'Driver_B');
INSERT INTO public.employee_roles VALUES ('e_2_b0_Driver_B', 'Driver_B');
INSERT INTO public.employee_roles VALUES ('e_3_b0_Driver_B', 'Driver_B');
INSERT INTO public.employee_roles VALUES ('e_1_b0_Driver_C', 'Driver_C');
INSERT INTO public.employee_roles VALUES ('e_2_b0_Driver_C', 'Driver_C');
INSERT INTO public.employee_roles VALUES ('e_3_b0_Driver_C', 'Driver_C');
INSERT INTO public.employee_roles VALUES ('e_1_b0_Driver_D', 'Driver_D');
INSERT INTO public.employee_roles VALUES ('e_2_b0_Driver_D', 'Driver_D');
INSERT INTO public.employee_roles VALUES ('e_3_b0_Driver_D', 'Driver_D');
INSERT INTO public.employee_roles VALUES ('Hr_0', 'HrManager');
INSERT INTO public.employee_roles VALUES ('DriverManager_0', 'DriverManager');
INSERT INTO public.employee_roles VALUES ('e_1_b1_ShiftManager', 'ShiftManager');
INSERT INTO public.employee_roles VALUES ('e_2_b1_ShiftManager', 'ShiftManager');
INSERT INTO public.employee_roles VALUES ('e_3_b1_ShiftManager', 'ShiftManager');
INSERT INTO public.employee_roles VALUES ('e_1_b1_Cashier', 'Cashier');
INSERT INTO public.employee_roles VALUES ('e_2_b1_Cashier', 'Cashier');
INSERT INTO public.employee_roles VALUES ('e_3_b1_Cashier', 'Cashier');
INSERT INTO public.employee_roles VALUES ('e_1_b1_StoreKeeper', 'StoreKeeper');
INSERT INTO public.employee_roles VALUES ('e_2_b1_StoreKeeper', 'StoreKeeper');
INSERT INTO public.employee_roles VALUES ('e_3_b1_StoreKeeper', 'StoreKeeper');
INSERT INTO public.employee_roles VALUES ('e_1_b1_Driver_A', 'Driver_A');
INSERT INTO public.employee_roles VALUES ('e_2_b1_Driver_A', 'Driver_A');
INSERT INTO public.employee_roles VALUES ('e_3_b1_Driver_A', 'Driver_A');
INSERT INTO public.employee_roles VALUES ('e_1_b1_Driver_B', 'Driver_B');
INSERT INTO public.employee_roles VALUES ('e_2_b1_Driver_B', 'Driver_B');
INSERT INTO public.employee_roles VALUES ('e_3_b1_Driver_B', 'Driver_B');
INSERT INTO public.employee_roles VALUES ('e_1_b1_Driver_C', 'Driver_C');
INSERT INTO public.employee_roles VALUES ('e_2_b1_Driver_C', 'Driver_C');
INSERT INTO public.employee_roles VALUES ('e_3_b1_Driver_C', 'Driver_C');
INSERT INTO public.employee_roles VALUES ('e_1_b1_Driver_D', 'Driver_D');
INSERT INTO public.employee_roles VALUES ('e_2_b1_Driver_D', 'Driver_D');
INSERT INTO public.employee_roles VALUES ('e_3_b1_Driver_D', 'Driver_D');
INSERT INTO public.employee_roles VALUES ('Hr_1', 'HrManager');
INSERT INTO public.employee_roles VALUES ('DriverManager_1', 'DriverManager');
INSERT INTO public.employee_roles VALUES ('TEST', 'ShiftManager');
INSERT INTO public.employee_roles VALUES ('TEST', 'Cashier');
INSERT INTO public.employee_roles VALUES ('TEST', 'StoreKeeper');
INSERT INTO public.employee_roles VALUES ('TEST', 'Driver_A');
INSERT INTO public.employee_roles VALUES ('TEST', 'Driver_B');
INSERT INTO public.employee_roles VALUES ('TEST', 'Driver_C');
INSERT INTO public.employee_roles VALUES ('TEST', 'Driver_D');
INSERT INTO public.employee_roles VALUES ('TEST', 'DriverManager');
INSERT INTO public.employee_roles VALUES ('FIRE_HIM', 'ShiftManager');
INSERT INTO public.employee_roles VALUES ('FIRE_HIM', 'Cashier');
INSERT INTO public.employee_roles VALUES ('FIRE_HIM', 'StoreKeeper');
INSERT INTO public.employee_roles VALUES ('FIRE_HIM', 'Driver_A');
INSERT INTO public.employee_roles VALUES ('FIRE_HIM', 'Driver_B');
INSERT INTO public.employee_roles VALUES ('FIRE_HIM', 'Driver_C');
INSERT INTO public.employee_roles VALUES ('FIRE_HIM', 'Driver_D');
INSERT INTO public.employee_roles VALUES ('FIRE_HIM', 'DriverManager');
INSERT INTO public.employee_roles VALUES ('FIRE', 'ShiftManager');
INSERT INTO public.employee_roles VALUES ('FIRE', 'Cashier');
INSERT INTO public.employee_roles VALUES ('FIRE', 'StoreKeeper');
INSERT INTO public.employee_roles VALUES ('FIRE', 'Driver_A');
INSERT INTO public.employee_roles VALUES ('FIRE', 'Driver_B');
INSERT INTO public.employee_roles VALUES ('FIRE', 'Driver_C');
INSERT INTO public.employee_roles VALUES ('FIRE', 'Driver_D');
INSERT INTO public.employee_roles VALUES ('FIRE', 'DriverManager');
INSERT INTO public.employee_roles VALUES ('Hr_0', 'ShiftManager');
INSERT INTO public.employee_roles VALUES ('Hr_0', 'Cashier');
INSERT INTO public.employee_roles VALUES ('Hr_0', 'StoreKeeper');
INSERT INTO public.employee_roles VALUES ('Hr_0', 'Driver_A');
INSERT INTO public.employee_roles VALUES ('Hr_0', 'Driver_B');
INSERT INTO public.employee_roles VALUES ('Hr_0', 'Driver_C');
INSERT INTO public.employee_roles VALUES ('Hr_0', 'Driver_D');
INSERT INTO public.employee_roles VALUES ('Hr_0', 'DriverManager');


--
-- TOC entry 4878 (class 0 OID 17486)
-- Dependencies: 226
-- Data for Name: employees; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.employees VALUES ('e_3_b0_ShiftManager', 'e_3_b0_ShiftManager', 0, false, 'IL003SHI', true, false, 50, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_2_b0_Cashier', 'e_2_b0_Cashier', 0, false, 'IL002CAS', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_2_b0_StoreKeeper', 'e_2_b0_StoreKeeper', 0, false, 'IL002STO', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_3_b0_StoreKeeper', 'e_3_b0_StoreKeeper', 0, false, 'IL003STO', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_1_b0_Driver_A', 'e_1_b0_Driver_A', 0, false, 'IL001DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_2_b0_Driver_A', 'e_2_b0_Driver_A', 0, false, 'IL002DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_3_b0_Driver_A', 'e_3_b0_Driver_A', 0, false, 'IL003DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_1_b0_Driver_B', 'e_1_b0_Driver_B', 0, false, 'IL001DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_2_b0_Driver_B', 'e_2_b0_Driver_B', 0, false, 'IL002DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_3_b0_Driver_B', 'e_3_b0_Driver_B', 0, false, 'IL003DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_1_b0_Driver_C', 'e_1_b0_Driver_C', 0, false, 'IL001DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_2_b0_Driver_C', 'e_2_b0_Driver_C', 0, false, 'IL002DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_3_b0_Driver_C', 'e_3_b0_Driver_C', 0, false, 'IL003DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_1_b0_Driver_D', 'e_1_b0_Driver_D', 0, false, 'IL001DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_2_b0_Driver_D', 'e_2_b0_Driver_D', 0, false, 'IL002DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_3_b0_Driver_D', 'e_3_b0_Driver_D', 0, false, 'IL003DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_1_b1_ShiftManager', 'e_1_b1_ShiftManager', 1, false, 'IL101SHI', true, false, 50, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_2_b1_ShiftManager', 'e_2_b1_ShiftManager', 1, false, 'IL102SHI', true, false, 50, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_3_b1_ShiftManager', 'e_3_b1_ShiftManager', 1, false, 'IL103SHI', true, false, 50, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_1_b1_Cashier', 'e_1_b1_Cashier', 1, false, 'IL101CAS', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_2_b1_Cashier', 'e_2_b1_Cashier', 1, false, 'IL102CAS', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_3_b1_Cashier', 'e_3_b1_Cashier', 1, false, 'IL103CAS', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_1_b1_StoreKeeper', 'e_1_b1_StoreKeeper', 1, false, 'IL101STO', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_2_b1_StoreKeeper', 'e_2_b1_StoreKeeper', 1, false, 'IL102STO', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_3_b1_StoreKeeper', 'e_3_b1_StoreKeeper', 1, false, 'IL103STO', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_1_b1_Driver_A', 'e_1_b1_Driver_A', 1, false, 'IL101DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_2_b1_Driver_A', 'e_2_b1_Driver_A', 1, false, 'IL102DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_3_b1_Driver_A', 'e_3_b1_Driver_A', 1, false, 'IL103DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_1_b1_Driver_B', 'e_1_b1_Driver_B', 1, false, 'IL101DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_2_b1_Driver_B', 'e_2_b1_Driver_B', 1, false, 'IL102DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_3_b1_Driver_B', 'e_3_b1_Driver_B', 1, false, 'IL103DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_1_b1_Driver_C', 'e_1_b1_Driver_C', 1, false, 'IL101DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_2_b1_Driver_C', 'e_2_b1_Driver_C', 1, false, 'IL102DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_3_b1_Driver_C', 'e_3_b1_Driver_C', 1, false, 'IL103DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_1_b1_Driver_D', 'e_1_b1_Driver_D', 1, false, 'IL101DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_2_b1_Driver_D', 'e_2_b1_Driver_D', 1, false, 'IL102DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_3_b1_Driver_D', 'e_3_b1_Driver_D', 1, false, 'IL103DRI', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('Hr_1', 'Hr_1', 1, false, 'IL101HRM', false, true, 70, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('DriverManager_1', 'DriverManager_1', 1, false, 'IL101DRI', false, false, 50, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_1_b0_StoreKeeper', 'e_1_b0_StoreKeeper', 0, false, 'IL001STO', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_1_b0_ShiftManager', 'e_1_b0_ShiftManager', 0, false, 'IL001SHI', true, false, 50, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_2_b0_ShiftManager', 'e_2_b0_ShiftManager', 0, false, 'IL002SHI', true, false, 50, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('e_1_b0_Cashier', 'e_1_b0_Cashier', 0, false, 'IL001CAS', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('FIRE_HIM', '1234', 0, false, '123', true, false, 50, '2025-05-31', '2025-05-31', '2025-05-31');
INSERT INTO public.employees VALUES ('FIRE', '1234', 1, false, '1234', true, false, 45, '2025-05-31', '2025-05-31', '2025-05-31');
INSERT INTO public.employees VALUES ('e_3_b0_Cashier', 'e_3_b0_Cashier', 0, false, 'IL003CAS', false, false, 40, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('TEST', 'TEST', 0, false, '123', true, false, 50, '2025-05-27', '2026-05-27', NULL);
INSERT INTO public.employees VALUES ('Hr_0', 'Hr_0', 0, false, 'IL001HRM', true, true, 70, '2025-01-01', '2025-12-31', NULL);
INSERT INTO public.employees VALUES ('DriverManager_0', 'DriverManager_0', 0, true, 'IL001DRI', false, false, 50, '2025-01-01', '2025-12-31', NULL);


--
-- TOC entry 4879 (class 0 OID 17489)
-- Dependencies: 227
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.roles VALUES ('ShiftManager');
INSERT INTO public.roles VALUES ('Cashier');
INSERT INTO public.roles VALUES ('StoreKeeper');
INSERT INTO public.roles VALUES ('Driver_A');
INSERT INTO public.roles VALUES ('Driver_B');
INSERT INTO public.roles VALUES ('Driver_C');
INSERT INTO public.roles VALUES ('Driver_D');
INSERT INTO public.roles VALUES ('DriverManager');
INSERT INTO public.roles VALUES ('HrManager');


--
-- TOC entry 4880 (class 0 OID 17492)
-- Dependencies: 228
-- Data for Name: shift_requirements; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 0, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 0, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 0, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 0, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 0, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 0, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 0, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 0, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 0, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 1, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 1, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 1, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 1, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 1, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 1, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 1, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 1, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 1, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 2, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 2, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 2, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 2, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 2, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 2, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 2, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 2, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 2, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 3, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 3, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 3, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 3, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 3, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 3, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 3, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 3, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 3, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 4, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 4, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 4, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 4, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 4, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 4, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 4, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 4, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 4, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 5, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 5, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 5, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 5, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 5, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 5, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 5, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 5, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 5, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 6, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 6, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 6, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 6, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 6, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 6, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 6, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 6, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 6, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 7, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 7, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 7, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 7, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 7, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 7, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 7, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 7, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 7, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 8, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 8, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 8, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 8, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 8, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 8, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 8, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 8, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 8, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 9, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 9, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 9, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 9, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 9, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 9, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 9, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 9, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 9, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 10, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 10, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 10, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 10, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 10, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 10, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 10, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 10, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 10, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 11, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 11, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 11, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 11, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 11, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 11, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 11, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 11, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 11, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 12, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 12, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 12, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 12, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 12, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 12, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 12, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 12, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 12, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 13, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 13, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 13, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 13, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 13, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 13, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 13, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 13, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-05-25', 13, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 0, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 0, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 0, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 0, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 0, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 0, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 0, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 0, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 0, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 1, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 1, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 1, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 1, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 1, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 1, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 1, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 1, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 1, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 2, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 2, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 2, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 2, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 2, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 2, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 2, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 2, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 2, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 3, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 3, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 3, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 3, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 3, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 3, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 3, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 3, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 3, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 4, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 4, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 4, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 4, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 4, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 4, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 4, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 4, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 4, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 5, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 5, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 5, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 5, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 5, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 5, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 5, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 5, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 5, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 6, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 6, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 6, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 6, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 6, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 6, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 6, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 6, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 6, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 7, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 7, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 7, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 7, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 7, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 7, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 7, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 7, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 7, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 8, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 8, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 8, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 8, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 8, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 8, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 8, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 8, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 8, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 9, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 9, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 9, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 9, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 9, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 9, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 9, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 9, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 9, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 10, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 10, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 10, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 10, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 10, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 10, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 10, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 10, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 10, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 11, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 11, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 11, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 11, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 11, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 11, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 11, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 11, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 11, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 12, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 12, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 12, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 12, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 12, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 12, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 12, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 12, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 12, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 13, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 13, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 13, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 13, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 13, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 13, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 13, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 13, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-05-25', 13, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 0, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 0, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 0, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 1, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 1, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 1, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 2, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 2, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 2, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 0, 'HrManager', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 0, 'Driver_D', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 0, 'Driver_C', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 0, 'Cashier', 2);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 1, 'StoreKeeper', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 1, 'Driver_A', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 1, 'Driver_B', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 1, 'Driver_C', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 1, 'Driver_D', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 1, 'DriverManager', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 2, 'Driver_A', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 2, 'Driver_C', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 3, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 3, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 4, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 4, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 4, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 5, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 5, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 6, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 6, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 6, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 6, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 6, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 6, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 6, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 6, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 6, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 7, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 7, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 7, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 7, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 7, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 7, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 7, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 7, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 8, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 8, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 8, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 9, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 9, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 9, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 9, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 9, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 9, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 9, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 9, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 9, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 10, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 10, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 10, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 10, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 10, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 10, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 10, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 10, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 10, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 11, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 11, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 11, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 11, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 11, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 11, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 11, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 11, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 11, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 12, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 12, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 12, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 12, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 12, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 12, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 12, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 12, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 12, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 13, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 13, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 13, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 0, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 0, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 0, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 0, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 0, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 0, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 0, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 0, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 0, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 1, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 1, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 1, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 1, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 1, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 1, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 1, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 1, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 1, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 2, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 2, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 2, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 2, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 2, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 2, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 2, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 2, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 2, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 3, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 3, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 3, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 3, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 3, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 3, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 3, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 3, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 3, 'Driver_A', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 3, 'Driver_C', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 3, 'Driver_B', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 2, 'DriverManager', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 4, 'Driver_A', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 4, 'Driver_B', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 4, 'Driver_C', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 4, 'Driver_D', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 4, 'DriverManager', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 5, 'Cashier', 2);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 5, 'HrManager', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 5, 'Driver_A', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 5, 'Driver_B', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 5, 'Driver_C', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 5, 'Driver_D', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 7, 'HrManager', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 8, 'Driver_A', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 8, 'Driver_B', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 8, 'Driver_D', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 8, 'DriverManager', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 13, 'HrManager', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 13, 'Driver_A', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 13, 'Driver_B', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 13, 'Driver_C', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 13, 'Driver_D', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 13, 'DriverManager', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 2, 'Driver_D', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 5, 'StoreKeeper', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 8, 'HrManager', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 3, 'StoreKeeper', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 2, 'Cashier', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 2, 'StoreKeeper', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 3, 'Cashier', 0);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 3, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 4, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 4, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 4, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 4, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 4, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 4, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 4, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 4, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 4, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 5, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 5, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 5, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 5, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 5, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 5, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 5, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 5, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 5, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 6, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 6, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 6, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 6, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 6, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 6, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 6, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 6, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 6, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 7, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 7, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 7, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 7, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 7, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 7, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 7, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 7, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 7, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 8, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 8, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 8, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 8, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 8, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 8, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 8, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 8, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 8, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 9, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 9, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 9, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 9, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 9, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 9, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 9, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 9, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 9, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 10, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 10, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 10, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 10, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 10, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 10, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 10, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 10, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 10, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 11, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 11, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 11, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 11, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 11, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 11, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 11, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 11, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 11, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 12, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 12, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 12, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 12, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 12, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 12, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 12, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 12, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 12, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 13, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 13, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 13, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 13, 'Driver_B', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 13, 'Driver_C', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 13, 'Driver_D', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 13, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 13, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-01', 13, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 0, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 0, 'Driver_B', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 3, 'HrManager', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 4, 'HrManager', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 8, 'Driver_C', 0);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-01', 3, 'DriverManager', 0);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 0, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 0, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 0, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 0, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 1, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 1, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 1, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 1, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 2, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 2, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 2, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 2, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 3, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 3, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 3, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 3, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 4, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 4, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 4, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 4, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 5, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 5, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 5, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 5, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 6, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 6, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 6, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 6, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 7, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 7, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 7, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 7, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 8, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 8, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 8, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 8, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 9, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 9, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 9, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 9, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 10, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 10, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 10, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 10, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 11, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 11, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 11, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 11, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 12, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 12, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 12, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 12, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 13, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 13, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 13, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 13, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 0, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 0, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 0, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 0, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 1, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 1, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 1, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 1, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 2, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 2, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 2, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 2, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 3, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 3, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 3, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 3, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 4, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 4, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 4, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 4, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 5, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 5, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 5, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 5, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 6, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 6, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 6, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 6, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 7, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 7, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 7, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 7, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 8, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 8, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 8, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 8, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 9, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 9, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 9, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 9, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 10, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 10, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 10, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 10, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 11, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 11, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 11, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 11, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 12, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 12, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 12, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 12, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 13, 'Cashier', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 13, 'DriverManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 13, 'HrManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 13, 'ShiftManager', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 0, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 0, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 8, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 8, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 8, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 9, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 9, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 9, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 6, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-08', 6, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-08', 6, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-15', 9, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-15', 9, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-15', 9, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-15', 0, 'Driver_A', 1);
INSERT INTO public.shift_requirements VALUES (0, '2025-06-15', 0, 'StoreKeeper', 1);
INSERT INTO public.shift_requirements VALUES (1, '2025-06-15', 0, 'StoreKeeper', 1);


--
-- TOC entry 4881 (class 0 OID 17495)
-- Dependencies: 229
-- Data for Name: shifts; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.shifts VALUES (0, '2025-06-15', '2025-06-15', '08:00:00', '15:00:00', 0, false, 'morning', false);
INSERT INTO public.shifts VALUES (3, '2025-06-15', '2025-06-16', '15:00:00', '20:00:00', 0, false, 'evening', false);
INSERT INTO public.shifts VALUES (6, '2025-06-15', '2025-06-18', '08:00:00', '15:00:00', 0, false, 'morning', false);
INSERT INTO public.shifts VALUES (9, '2025-06-15', '2025-06-19', '15:00:00', '20:00:00', 0, false, 'evening', false);
INSERT INTO public.shifts VALUES (12, '2025-06-15', '2025-06-21', '08:00:00', '15:00:00', 0, false, 'morning', false);
INSERT INTO public.shifts VALUES (3, '2025-06-08', '2025-06-09', '15:00:00', '20:00:00', 0, false, 'evening', false);
INSERT INTO public.shifts VALUES (12, '2025-06-08', '2025-06-14', '08:00:00', '15:00:00', 0, false, 'morning', false);
INSERT INTO public.shifts VALUES (0, '2025-05-25', '2025-05-25', '08:00:00', '15:00:00', 0, false, 'morning', true);
INSERT INTO public.shifts VALUES (1, '2025-05-25', '2025-05-25', '15:00:00', '20:00:00', 0, false, 'evening', true);
INSERT INTO public.shifts VALUES (2, '2025-05-25', '2025-05-26', '08:00:00', '15:00:00', 0, false, 'morning', true);
INSERT INTO public.shifts VALUES (3, '2025-05-25', '2025-05-26', '15:00:00', '20:00:00', 0, false, 'evening', true);
INSERT INTO public.shifts VALUES (4, '2025-05-25', '2025-05-27', '08:00:00', '15:00:00', 0, false, 'morning', true);
INSERT INTO public.shifts VALUES (5, '2025-05-25', '2025-05-27', '15:00:00', '20:00:00', 0, false, 'evening', true);
INSERT INTO public.shifts VALUES (6, '2025-05-25', '2025-05-28', '08:00:00', '15:00:00', 0, false, 'morning', true);
INSERT INTO public.shifts VALUES (7, '2025-05-25', '2025-05-28', '15:00:00', '20:00:00', 0, false, 'evening', true);
INSERT INTO public.shifts VALUES (8, '2025-05-25', '2025-05-29', '08:00:00', '15:00:00', 0, false, 'morning', true);
INSERT INTO public.shifts VALUES (9, '2025-05-25', '2025-05-29', '15:00:00', '20:00:00', 0, false, 'evening', true);
INSERT INTO public.shifts VALUES (10, '2025-05-25', '2025-05-30', '08:00:00', '15:00:00', 0, false, 'morning', true);
INSERT INTO public.shifts VALUES (11, '2025-05-25', '2025-05-30', '15:00:00', '20:00:00', 0, false, 'evening', true);
INSERT INTO public.shifts VALUES (12, '2025-05-25', '2025-05-31', '08:00:00', '15:00:00', 0, false, 'morning', true);
INSERT INTO public.shifts VALUES (13, '2025-05-25', '2025-05-31', '15:00:00', '20:00:00', 0, false, 'evening', true);
INSERT INTO public.shifts VALUES (0, '2025-05-25', '2025-05-25', '08:00:00', '15:00:00', 1, false, 'morning', true);
INSERT INTO public.shifts VALUES (1, '2025-05-25', '2025-05-25', '15:00:00', '20:00:00', 1, false, 'evening', true);
INSERT INTO public.shifts VALUES (2, '2025-05-25', '2025-05-26', '08:00:00', '15:00:00', 1, false, 'morning', true);
INSERT INTO public.shifts VALUES (3, '2025-05-25', '2025-05-26', '15:00:00', '20:00:00', 1, false, 'evening', true);
INSERT INTO public.shifts VALUES (4, '2025-05-25', '2025-05-27', '08:00:00', '15:00:00', 1, false, 'morning', true);
INSERT INTO public.shifts VALUES (5, '2025-05-25', '2025-05-27', '15:00:00', '20:00:00', 1, false, 'evening', true);
INSERT INTO public.shifts VALUES (6, '2025-05-25', '2025-05-28', '08:00:00', '15:00:00', 1, false, 'morning', true);
INSERT INTO public.shifts VALUES (7, '2025-05-25', '2025-05-28', '15:00:00', '20:00:00', 1, false, 'evening', true);
INSERT INTO public.shifts VALUES (8, '2025-05-25', '2025-05-29', '08:00:00', '15:00:00', 1, false, 'morning', true);
INSERT INTO public.shifts VALUES (9, '2025-05-25', '2025-05-29', '15:00:00', '20:00:00', 1, false, 'evening', true);
INSERT INTO public.shifts VALUES (10, '2025-05-25', '2025-05-30', '08:00:00', '15:00:00', 1, false, 'morning', true);
INSERT INTO public.shifts VALUES (11, '2025-05-25', '2025-05-30', '15:00:00', '20:00:00', 1, false, 'evening', true);
INSERT INTO public.shifts VALUES (12, '2025-05-25', '2025-05-31', '08:00:00', '15:00:00', 1, false, 'morning', true);
INSERT INTO public.shifts VALUES (13, '2025-05-25', '2025-05-31', '15:00:00', '20:00:00', 1, false, 'evening', true);
INSERT INTO public.shifts VALUES (0, '2025-06-01', '2025-06-01', '08:00:00', '15:00:00', 0, false, 'morning', true);
INSERT INTO public.shifts VALUES (1, '2025-06-01', '2025-06-01', '15:00:00', '20:00:00', 0, false, 'evening', true);
INSERT INTO public.shifts VALUES (1, '2025-06-08', '2025-06-08', '15:00:00', '20:00:00', 0, false, 'evening', false);
INSERT INTO public.shifts VALUES (2, '2025-06-01', '2025-06-02', '08:00:00', '15:00:00', 0, false, 'morning', true);
INSERT INTO public.shifts VALUES (3, '2025-06-01', '2025-06-02', '15:00:00', '20:00:00', 0, false, 'evening', true);
INSERT INTO public.shifts VALUES (4, '2025-06-01', '2025-06-03', '08:00:00', '15:00:00', 0, false, 'morning', true);
INSERT INTO public.shifts VALUES (4, '2025-06-08', '2025-06-10', '08:00:00', '15:00:00', 0, false, 'morning', false);
INSERT INTO public.shifts VALUES (5, '2025-06-01', '2025-06-03', '15:00:00', '20:00:00', 0, false, 'evening', true);
INSERT INTO public.shifts VALUES (6, '2025-06-01', '2025-06-04', '08:00:00', '15:00:00', 0, true, 'morning', true);
INSERT INTO public.shifts VALUES (7, '2025-06-01', '2025-06-04', '15:00:00', '20:00:00', 0, true, 'evening', true);
INSERT INTO public.shifts VALUES (7, '2025-06-08', '2025-06-11', '15:00:00', '20:00:00', 0, false, 'evening', false);
INSERT INTO public.shifts VALUES (10, '2025-06-08', '2025-06-13', '08:00:00', '15:00:00', 0, false, 'morning', false);
INSERT INTO public.shifts VALUES (13, '2025-06-08', '2025-06-14', '15:00:00', '20:00:00', 0, false, 'evening', false);
INSERT INTO public.shifts VALUES (1, '2025-06-15', '2025-06-15', '15:00:00', '20:00:00', 0, false, 'evening', false);
INSERT INTO public.shifts VALUES (4, '2025-06-15', '2025-06-17', '08:00:00', '15:00:00', 0, false, 'morning', false);
INSERT INTO public.shifts VALUES (7, '2025-06-15', '2025-06-18', '15:00:00', '20:00:00', 0, false, 'evening', false);
INSERT INTO public.shifts VALUES (10, '2025-06-15', '2025-06-20', '08:00:00', '15:00:00', 0, false, 'morning', false);
INSERT INTO public.shifts VALUES (13, '2025-06-15', '2025-06-21', '15:00:00', '20:00:00', 0, false, 'evening', false);
INSERT INTO public.shifts VALUES (2, '2025-06-08', '2025-06-09', '08:00:00', '15:00:00', 0, false, 'morning', false);
INSERT INTO public.shifts VALUES (5, '2025-06-08', '2025-06-10', '15:00:00', '20:00:00', 0, false, 'evening', false);
INSERT INTO public.shifts VALUES (8, '2025-06-01', '2025-06-05', '08:00:00', '15:00:00', 0, false, 'morning', true);
INSERT INTO public.shifts VALUES (9, '2025-06-01', '2025-06-05', '15:00:00', '20:00:00', 0, true, 'evening', true);
INSERT INTO public.shifts VALUES (10, '2025-06-01', '2025-06-06', '08:00:00', '15:00:00', 0, true, 'morning', true);
INSERT INTO public.shifts VALUES (11, '2025-06-01', '2025-06-06', '15:00:00', '20:00:00', 0, true, 'evening', true);
INSERT INTO public.shifts VALUES (11, '2025-06-08', '2025-06-13', '15:00:00', '20:00:00', 0, false, 'evening', false);
INSERT INTO public.shifts VALUES (12, '2025-06-01', '2025-06-07', '08:00:00', '15:00:00', 0, true, 'morning', true);
INSERT INTO public.shifts VALUES (13, '2025-06-01', '2025-06-07', '17:00:00', '22:00:00', 0, false, 'evening', true);
INSERT INTO public.shifts VALUES (2, '2025-06-15', '2025-06-16', '08:00:00', '15:00:00', 0, false, 'morning', false);
INSERT INTO public.shifts VALUES (5, '2025-06-15', '2025-06-17', '15:00:00', '20:00:00', 0, false, 'evening', false);
INSERT INTO public.shifts VALUES (8, '2025-06-15', '2025-06-19', '08:00:00', '15:00:00', 0, false, 'morning', false);
INSERT INTO public.shifts VALUES (11, '2025-06-15', '2025-06-20', '15:00:00', '20:00:00', 0, false, 'evening', false);
INSERT INTO public.shifts VALUES (0, '2025-06-08', '2025-06-08', '08:00:00', '15:00:00', 1, false, 'morning', false);
INSERT INTO public.shifts VALUES (1, '2025-06-01', '2025-06-01', '15:00:00', '20:00:00', 1, false, 'evening', true);
INSERT INTO public.shifts VALUES (2, '2025-06-01', '2025-06-02', '08:00:00', '15:00:00', 1, false, 'morning', true);
INSERT INTO public.shifts VALUES (3, '2025-06-01', '2025-06-02', '15:00:00', '20:00:00', 1, false, 'evening', true);
INSERT INTO public.shifts VALUES (4, '2025-06-01', '2025-06-03', '08:00:00', '15:00:00', 1, false, 'morning', true);
INSERT INTO public.shifts VALUES (5, '2025-06-01', '2025-06-03', '15:00:00', '20:00:00', 1, false, 'evening', true);
INSERT INTO public.shifts VALUES (6, '2025-06-01', '2025-06-04', '08:00:00', '15:00:00', 1, false, 'morning', true);
INSERT INTO public.shifts VALUES (7, '2025-06-01', '2025-06-04', '15:00:00', '20:00:00', 1, false, 'evening', true);
INSERT INTO public.shifts VALUES (8, '2025-06-01', '2025-06-05', '08:00:00', '15:00:00', 1, false, 'morning', true);
INSERT INTO public.shifts VALUES (9, '2025-06-01', '2025-06-05', '15:00:00', '20:00:00', 1, false, 'evening', true);
INSERT INTO public.shifts VALUES (10, '2025-06-01', '2025-06-06', '08:00:00', '15:00:00', 1, false, 'morning', true);
INSERT INTO public.shifts VALUES (11, '2025-06-01', '2025-06-06', '15:00:00', '20:00:00', 1, false, 'evening', true);
INSERT INTO public.shifts VALUES (12, '2025-06-01', '2025-06-07', '08:00:00', '15:00:00', 1, false, 'morning', true);
INSERT INTO public.shifts VALUES (13, '2025-06-01', '2025-06-07', '15:00:00', '20:00:00', 1, false, 'evening', true);
INSERT INTO public.shifts VALUES (0, '2025-06-01', '2025-06-01', '08:00:00', '15:00:00', 1, false, 'morning', true);
INSERT INTO public.shifts VALUES (1, '2025-06-08', '2025-06-08', '15:00:00', '20:00:00', 1, false, 'evening', false);
INSERT INTO public.shifts VALUES (2, '2025-06-08', '2025-06-09', '08:00:00', '15:00:00', 1, false, 'morning', false);
INSERT INTO public.shifts VALUES (3, '2025-06-08', '2025-06-09', '15:00:00', '20:00:00', 1, false, 'evening', false);
INSERT INTO public.shifts VALUES (4, '2025-06-08', '2025-06-10', '08:00:00', '15:00:00', 1, false, 'morning', false);
INSERT INTO public.shifts VALUES (5, '2025-06-08', '2025-06-10', '15:00:00', '20:00:00', 1, false, 'evening', false);
INSERT INTO public.shifts VALUES (6, '2025-06-08', '2025-06-11', '08:00:00', '15:00:00', 1, false, 'morning', false);
INSERT INTO public.shifts VALUES (7, '2025-06-08', '2025-06-11', '15:00:00', '20:00:00', 1, false, 'evening', false);
INSERT INTO public.shifts VALUES (8, '2025-06-08', '2025-06-12', '08:00:00', '15:00:00', 1, false, 'morning', false);
INSERT INTO public.shifts VALUES (9, '2025-06-08', '2025-06-12', '15:00:00', '20:00:00', 1, false, 'evening', false);
INSERT INTO public.shifts VALUES (10, '2025-06-08', '2025-06-13', '08:00:00', '15:00:00', 1, false, 'morning', false);
INSERT INTO public.shifts VALUES (11, '2025-06-08', '2025-06-13', '15:00:00', '20:00:00', 1, false, 'evening', false);
INSERT INTO public.shifts VALUES (12, '2025-06-08', '2025-06-14', '08:00:00', '15:00:00', 1, false, 'morning', false);
INSERT INTO public.shifts VALUES (13, '2025-06-08', '2025-06-14', '15:00:00', '20:00:00', 1, false, 'evening', false);
INSERT INTO public.shifts VALUES (0, '2025-06-15', '2025-06-15', '08:00:00', '15:00:00', 1, false, 'morning', false);
INSERT INTO public.shifts VALUES (1, '2025-06-15', '2025-06-15', '15:00:00', '20:00:00', 1, false, 'evening', false);
INSERT INTO public.shifts VALUES (2, '2025-06-15', '2025-06-16', '08:00:00', '15:00:00', 1, false, 'morning', false);
INSERT INTO public.shifts VALUES (3, '2025-06-15', '2025-06-16', '15:00:00', '20:00:00', 1, false, 'evening', false);
INSERT INTO public.shifts VALUES (4, '2025-06-15', '2025-06-17', '08:00:00', '15:00:00', 1, false, 'morning', false);
INSERT INTO public.shifts VALUES (5, '2025-06-15', '2025-06-17', '15:00:00', '20:00:00', 1, false, 'evening', false);
INSERT INTO public.shifts VALUES (6, '2025-06-15', '2025-06-18', '08:00:00', '15:00:00', 1, false, 'morning', false);
INSERT INTO public.shifts VALUES (7, '2025-06-15', '2025-06-18', '15:00:00', '20:00:00', 1, false, 'evening', false);
INSERT INTO public.shifts VALUES (8, '2025-06-15', '2025-06-19', '08:00:00', '15:00:00', 1, false, 'morning', false);
INSERT INTO public.shifts VALUES (8, '2025-06-08', '2025-06-12', '08:00:00', '15:00:00', 0, false, 'morning', false);
INSERT INTO public.shifts VALUES (9, '2025-06-08', '2025-06-12', '15:00:00', '20:00:00', 0, false, 'evening', false);
INSERT INTO public.shifts VALUES (6, '2025-06-08', '2025-06-11', '08:00:00', '15:00:00', 0, false, 'morning', false);
INSERT INTO public.shifts VALUES (9, '2025-06-15', '2025-06-19', '15:00:00', '20:00:00', 1, false, 'evening', false);
INSERT INTO public.shifts VALUES (10, '2025-06-15', '2025-06-20', '08:00:00', '15:00:00', 1, false, 'morning', false);
INSERT INTO public.shifts VALUES (11, '2025-06-15', '2025-06-20', '15:00:00', '20:00:00', 1, false, 'evening', false);
INSERT INTO public.shifts VALUES (12, '2025-06-15', '2025-06-21', '08:00:00', '15:00:00', 1, false, 'morning', false);
INSERT INTO public.shifts VALUES (13, '2025-06-15', '2025-06-21', '15:00:00', '20:00:00', 1, false, 'evening', false);
INSERT INTO public.shifts VALUES (0, '2025-06-08', '2025-06-08', '08:00:00', '15:00:00', 0, false, 'morning', false);


--
-- TOC entry 4882 (class 0 OID 17499)
-- Dependencies: 230
-- Data for Name: truck_schedule; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.truck_schedule VALUES ('TRUCK1', 0, 'Assigned', '65');


--
-- TOC entry 4883 (class 0 OID 17504)
-- Dependencies: 231
-- Data for Name: trucks; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.trucks VALUES ('TRUCK1', 'A', 3500, 10000, 'Volvo');
INSERT INTO public.trucks VALUES ('TRUCK2', 'B', 3000, 8000, 'Mercedes');
INSERT INTO public.trucks VALUES ('TRUCK3', 'C', 4000, 12000, 'TOYOTA');
INSERT INTO public.trucks VALUES ('TRUCK4', 'A', 100, 10000, 'MERCEDES');


--
-- TOC entry 4890 (class 0 OID 0)
-- Dependencies: 220
-- Name: deliveries_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.deliveries_id_seq', 65, true);


--
-- TOC entry 4696 (class 2606 OID 17511)
-- Name: assigned_employees assigned_employees_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assigned_employees
    ADD CONSTRAINT assigned_employees_pkey PRIMARY KEY (branch_id, week_start, shift_id, username, role);


--
-- TOC entry 4698 (class 2606 OID 17513)
-- Name: branches branches_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.branches
    ADD CONSTRAINT branches_pkey PRIMARY KEY (branch_id);


--
-- TOC entry 4700 (class 2606 OID 17515)
-- Name: employee_availability employee_availability_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_availability
    ADD CONSTRAINT employee_availability_pk PRIMARY KEY (username, branch_id, week_start, shift_id, role);


--
-- TOC entry 4702 (class 2606 OID 17517)
-- Name: employee_roles employee_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_roles
    ADD CONSTRAINT employee_roles_pkey PRIMARY KEY (username, role);


--
-- TOC entry 4704 (class 2606 OID 17519)
-- Name: employees employees_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT employees_pkey PRIMARY KEY (username);


--
-- TOC entry 4706 (class 2606 OID 17521)
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (role_name);


--
-- TOC entry 4708 (class 2606 OID 17523)
-- Name: shift_requirements shift_requirements_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shift_requirements
    ADD CONSTRAINT shift_requirements_pkey PRIMARY KEY (branch_id, week_start, shift_id, role);


--
-- TOC entry 4710 (class 2606 OID 17525)
-- Name: shifts shifts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shifts
    ADD CONSTRAINT shifts_pkey PRIMARY KEY (branch_id, week_start, shift_id);


--
-- TOC entry 4712 (class 2606 OID 17527)
-- Name: trucks trucks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trucks
    ADD CONSTRAINT trucks_pkey PRIMARY KEY (license_plate);


--
-- TOC entry 4713 (class 2606 OID 17528)
-- Name: assigned_employees assigned_employees_branch_id_week_start_shift_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assigned_employees
    ADD CONSTRAINT assigned_employees_branch_id_week_start_shift_id_fkey FOREIGN KEY (branch_id, week_start, shift_id) REFERENCES public.shifts(branch_id, week_start, shift_id);


--
-- TOC entry 4714 (class 2606 OID 17533)
-- Name: assigned_employees assigned_employees_username_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assigned_employees
    ADD CONSTRAINT assigned_employees_username_fkey FOREIGN KEY (username) REFERENCES public.employees(username);


--
-- TOC entry 4715 (class 2606 OID 17538)
-- Name: employee_availability employee_availability_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_availability
    ADD CONSTRAINT employee_availability_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branches(branch_id);


--
-- TOC entry 4716 (class 2606 OID 17543)
-- Name: employee_availability employee_availability_role_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_availability
    ADD CONSTRAINT employee_availability_role_fkey FOREIGN KEY (role) REFERENCES public.roles(role_name);


--
-- TOC entry 4717 (class 2606 OID 17548)
-- Name: employee_availability employee_availability_username_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_availability
    ADD CONSTRAINT employee_availability_username_fkey FOREIGN KEY (username) REFERENCES public.employees(username);


--
-- TOC entry 4718 (class 2606 OID 17553)
-- Name: employee_roles employee_roles_role_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_roles
    ADD CONSTRAINT employee_roles_role_fkey FOREIGN KEY (role) REFERENCES public.roles(role_name);


--
-- TOC entry 4719 (class 2606 OID 17558)
-- Name: employee_roles employee_roles_username_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_roles
    ADD CONSTRAINT employee_roles_username_fkey FOREIGN KEY (username) REFERENCES public.employees(username);


--
-- TOC entry 4720 (class 2606 OID 17563)
-- Name: employees employees_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT employees_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branches(branch_id);


--
-- TOC entry 4721 (class 2606 OID 17568)
-- Name: shift_requirements shift_requirements_branch_id_week_start_shift_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shift_requirements
    ADD CONSTRAINT shift_requirements_branch_id_week_start_shift_id_fkey FOREIGN KEY (branch_id, week_start, shift_id) REFERENCES public.shifts(branch_id, week_start, shift_id);


--
-- TOC entry 4722 (class 2606 OID 17573)
-- Name: shift_requirements shift_requirements_role_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shift_requirements
    ADD CONSTRAINT shift_requirements_role_fkey FOREIGN KEY (role) REFERENCES public.roles(role_name);


--
-- TOC entry 4723 (class 2606 OID 17578)
-- Name: shifts shifts_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shifts
    ADD CONSTRAINT shifts_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branches(branch_id);


-- Completed on 2025-06-03 01:11:40

--
-- PostgreSQL database dump complete
--

