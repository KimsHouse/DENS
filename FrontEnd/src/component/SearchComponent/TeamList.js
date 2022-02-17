import React, { useEffect, useState } from 'react';
import TeamCard from './TeamCard';
import Modal from './Modal';
import { Navigate } from 'react-router-dom';
import styled from 'styled-components';
export default function TeamList( props) {
    const [temp, setTemp] = useState([]);

    useEffect(() => {
       // console.log("tsettestset");
       // console.log(props.teamlist);
        setTemp(props.teamlist);
    });
    
    
    const [modalppen, setModalppen] = useState(false);
    
    const [modaltitle, setModaltitle] = useState("");
    const [modalcontent, setModalcontent] = useState("");
    const [leaderID, setleaderID] = useState("");
    
    const openModal = (title,content) => {
        setModalppen(true);
        setModaltitle(title);
        setModalcontent(content);
        // console.log(props);
    }
    const closeModal = () => {
        setModalppen(false);
    }
    
    const gomessanger = (messanger) => {
        setleaderID(messanger);
        Navigate(`/afterlogin/messager/${leaderID}`);
    }

    return (
        <ContainerT>
        <SectionName>회원님과 연관될만한 팀리스트</SectionName>
        <SearchBigBox >
            <>
                    <Modal open={modalppen} close={closeModal} content={modalcontent} gomessanger={gomessanger} header={modaltitle }>{ modaltitle}</Modal>
            </>
            {
                    temp.map((data) => { return <TeamCard click={ openModal} check={data.team_id} data= {data}/>})
            }
        </SearchBigBox>
        </ContainerT>
    )
}

const ContainerT = styled.div`
display : flex;
flex-direction : column;
align-items: center;
`
const SectionName = styled.span`

`

const SearchBigBox = styled.div`
margin-left: 100px;
    display: flex;
    background-color: white;
    border: 1px solid pick;
    border-radius : 5px;
    box-shadow : 0px 30px 40px #f18fa1;
    flex-flow: column nowrap;
    width: 30vw;
    height: 60vh;
    margin: 50px;
    overflow: scroll;
    &::-webkit-scrollbar{
        display:none;
    }
`