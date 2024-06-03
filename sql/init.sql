use time_db_prod;

drop table if exists student;
drop table if exists teacher;
drop table if exists record;
drop table if exists class_room;

create table class_room (
                            class_room_id bigint not null auto_increment,
                            create_date datetime(6),
                            modified_date datetime(6),
                            teacher_id bigint,
                            class_info varchar(255),
                            name varchar(255),
                            class_type enum ('ELEMENTARY','MIDDLE','HIGH'),
                            primary key (class_room_id)
) engine=InnoDB;

create table record (
                        view integer default 0 not null,
                        create_date datetime(6),
                        modified_date datetime(6),
                        record_id bigint not null auto_increment,
                        student_id bigint,
                        teacher_id bigint,
                        title varchar(255),
                        content TEXT,
                        status enum ('PUBLISHED','DELETED'),
                        primary key (record_id)
) engine=InnoDB;

create table student (
                         grade integer not null,
                         class_room_id bigint,
                         create_date datetime(6),
                         modified_date datetime(6),
                         quit_date datetime(6),
                         student_id bigint not null auto_increment,
                         teacher_id bigint,
                         detail_address varchar(255),
                         email varchar(255),
                         name varchar(255),
                         parent_name varchar(255),
                         parent_phone_number varchar(255),
                         password varchar(255),
                         phone_number varchar(255),
                         school_name varchar(255),
                         street_address varchar(255),
                         user_id varchar(255),
                         zip_code varchar(255),
                         attendance_status enum ('Y','N'),
                         class_type enum ('ELEMENTARY','MIDDLE','HIGH'),
                         gender enum ('MALE','FEMALE'),
                         member_type enum ('TEACHER','PARENT','STUDENT'),
                         provider enum ('GENERAL','KAKAO'),
                         primary key (student_id)
) engine=InnoDB

create table teacher (
                         create_date datetime(6),
                         modified_date datetime(6),
                         teacher_id bigint not null auto_increment,
                         email varchar(255),
                         name varchar(255),
                         password varchar(255),
                         phone_number varchar(255),
                         gender enum ('MALE','FEMALE'),
                         member_type enum ('TEACHER','PARENT','STUDENT'),
                         position enum ('CHIEF','TEACHER','ADMIN'),
                         primary key (teacher_id)
) engine=InnoDB

alter table student
    add constraint unique_student_email unique (email);

alter table class_room
    add constraint unique_class_room_teacher
        foreign key (teacher_id)
            references teacher (teacher_id);

alter table record
    add constraint unique_record_student
        foreign key (student_id)
            references student (student_id);

alter table record
    add constraint unique_record_teacher
        foreign key (teacher_id)
            references teacher (teacher_id);

alter table student
    add constraint unique_student_class_room
        foreign key (class_room_id)
            references class_room (class_room_id);

alter table student
    add constraint unique_student_teacher
        foreign key (teacher_id)
            references teacher (teacher_id);

-- delete from teacher where name = '대박샘';

-- insert into teacher (`create_date`, `email`, `gender`, `member_type`, `modified_date`, `name`, `password`, `phone_number`, `position`)
-- values (now(), 'pjj@time.com', 'MALE', 'TEACHER', now(), '대박샘', SHA2('1234', 256),'010-3434-5678', 'CHIEF');
