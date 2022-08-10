import styled from 'styled-components';

import dashboard_bg_1 from '@/assets/svg/dashboard_bg_1.svg';
import dashboard_bg_2 from '@/assets/svg/dashboard_bg_2.svg';
import dashboard_bg_3 from '@/assets/svg/dashboard_bg_3.svg';
import dashboard_bg_4 from '@/assets/svg/dashboard_bg_4.svg';

import background_1 from '@/assets/images/background_1.png';

export const Container = styled.div`
  height: 100vh;
  min-height: 100vh;

  overflow: auto;
  
  background: url(${background_1}) no-repeat center;
  background-size: cover;
  
  padding-top: 2.4rem;
`;

export const Content = styled.div`
  max-width: 32rem;
  width: 100%;

  height: ${window.innerHeight}px;

  margin: 0 auto 24px;

  padding: 0 3.2rem 128px;

  background: url(${dashboard_bg_1}) no-repeat top;

  @media(min-width: 768px) {
    max-width: 768px;
    background: url(${dashboard_bg_2}) no-repeat top;
  }

  @media(min-width: 1024px) {
    max-width: 1024px;

    height: 900px;

    padding-top: 32px;
    
    background: url(${dashboard_bg_3}) no-repeat top;
  }

  @media(min-width: 1400px) {
    max-width: 1400px;
    background: url(${dashboard_bg_4}) no-repeat top;
  }
`;

export const MainContent = styled.main`
  height: 100%;
  position: relative;

  @media(min-width: 768px) {
    padding: 0 32px;
  }

  @media(min-width: 1024px) {
    max-height: 90%;
  }
`;
